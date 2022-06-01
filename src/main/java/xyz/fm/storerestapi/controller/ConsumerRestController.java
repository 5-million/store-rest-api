package xyz.fm.storerestapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fm.storerestapi.dto.user.consumer.ConsumerInfo;
import xyz.fm.storerestapi.dto.user.consumer.ConsumerJoinRequest;
import xyz.fm.storerestapi.service.ConsumerService;

import javax.validation.Valid;

@RestController
@RequestMapping("consumer")
public class ConsumerRestController {

    private final ConsumerService consumerService;

    public ConsumerRestController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @PostMapping
    public ResponseEntity<ConsumerInfo> join(@Valid @RequestBody ConsumerJoinRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ConsumerInfo.of(consumerService.join(request.toEntity())));
    }
}
