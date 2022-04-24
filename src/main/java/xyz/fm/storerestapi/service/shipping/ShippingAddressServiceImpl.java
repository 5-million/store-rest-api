package xyz.fm.storerestapi.service.shipping;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fm.storerestapi.dto.shipping.ShippingAddressModifyRequest;
import xyz.fm.storerestapi.dto.shipping.ShippingAddressRegisterRequest;
import xyz.fm.storerestapi.entity.shipping.Address;
import xyz.fm.storerestapi.entity.shipping.ShippingAddress;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NotEntityOwnerException;
import xyz.fm.storerestapi.error.exception.NotFoundException;
import xyz.fm.storerestapi.repository.shipping.ShippingAddressRepository;

@Service
@Transactional
public class ShippingAddressServiceImpl implements ShippingAddressService {

    private final ShippingAddressRepository shippingAddressRepository;

    public ShippingAddressServiceImpl(ShippingAddressRepository shippingAddressRepository) {
        this.shippingAddressRepository = shippingAddressRepository;
    }

    @Override
    public ShippingAddress register(Consumer consumer, ShippingAddressRegisterRequest request) {
        Address address = new Address(request.getZipcode(), request.getAddress(), request.getDetailedAddress());
        ShippingAddress shippingAddress = new ShippingAddress.Builder(address)
                .defaultAddress(request.isDefaultAddress())
                .build();

        consumer.addShippingAddress(shippingAddress);
        return shippingAddress;
    }

    @Override
    public ShippingAddress getById(Long shippingAddressId) {
        return shippingAddressRepository.findById(shippingAddressId)
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_SHIPPING_ADDRESS));
    }

    @Override
    public void modify(String consumerEmail, ShippingAddressModifyRequest request) {
        ShippingAddress shippingAddress = getById(request.getShippingAddressId());

        if (!shippingAddress.isOwner(consumerEmail)) {
            throw new NotEntityOwnerException(Error.NO_PERMISSION, ErrorDetail.NOT_ENTITY_OWNER);
        }

        shippingAddress.modifyAll(
                new ShippingAddress.Builder(
                        new Address(request.getZipcode(), request.getAddress(), request.getDetailedAddress())
                ).defaultAddress(request.isDefaultAddress()).build()
        );
    }

    @Override
    public void delete(String consumerEmail, Long shippingAddressId) {
        ShippingAddress shippingAddress = getById(shippingAddressId);

        if (!shippingAddress.isOwner(consumerEmail)) {
            throw new NotEntityOwnerException(Error.NO_PERMISSION, ErrorDetail.NOT_ENTITY_OWNER);
        }

        shippingAddressRepository.delete(shippingAddress);
    }

    @Override
    public void designateDefaultShippingAddress(Consumer consumer, Long shippingAddressId) {
        if (!consumer.designateDefaultShippingAddress(shippingAddressId)) {
            throw new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_SHIPPING_ADDRESS);
        }
    }
}
