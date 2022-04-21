package xyz.fm.storerestapi.dto.product;

import java.util.*;
import java.util.stream.Collectors;

public class ProductList {

    private List<SimpleProduct> products = new ArrayList<>();

    public ProductList(List<SimpleProduct> products) {
        Map<Long, List<SimpleProduct>> collect = products.stream().collect(Collectors.groupingBy(SimpleProduct::getItemId));

        PriorityQueue<SimpleProduct> pqBySalesQuantity = new PriorityQueue<>((o1, o2) -> o2.getSalesQuantity() - o1.getSalesQuantity());
        collect.forEach((key, value) -> {
            PriorityQueue<SimpleProduct> pq = new PriorityQueue<>(Comparator.comparingInt(SimpleProduct::getDiscountPrice));
            pq.addAll(value);
            pqBySalesQuantity.add(pq.poll());
        });


        int rank = 1;
        while (!pqBySalesQuantity.isEmpty()) {
            SimpleProduct product = pqBySalesQuantity.poll();
            product.setRank(rank);
            this.products.add(product);

            rank++;
        }
    }

    public List<SimpleProduct> getProducts() {
        return products;
    }
}
