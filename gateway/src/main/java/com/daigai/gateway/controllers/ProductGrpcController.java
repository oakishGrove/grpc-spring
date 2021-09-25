package com.daigai.gateway.controllers;

import com.daigai.gateway.dto.ProductResponseDto;
import com.daigai.gateway.service.ProductGrpcAsyncService;
import com.daigai.gateway.service.ProductGrpcSyncService;
//import lombok.AllArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController("/api/grpc")
@AllArgsConstructor
public class ProductGrpcController {

    private static final String ASYNC = "/async";
    private static final String SYNC = "/sync";

    private final ProductGrpcAsyncService asyncService;
    private final ProductGrpcSyncService syncService;

//    public ProductGrpcController(ProductGrpcAsyncService asyncService, ProductGrpcSyncService syncService) {
//        this.asyncService = asyncService;
//        this.syncService = syncService;
//    }

//    @GetMapping(ASYNC + "/products")
//    public List<Object> getProductsASync() {
//        return asyncService.getProductsASync();
//    }

    @GetMapping(value = SYNC + "/products")
    public List<ProductResponseDto> getProductsSync() {
        return syncService.getProducts();
    }

    @GetMapping(SYNC + "/products/{id}")
    public ProductResponseDto getProductsSync(@PathVariable("id") long id) {
        return syncService.getProductById(id);
    }


    @GetMapping(ASYNC + "/products")
//    public ProductResponseDto getProductsASync(@PathVariable("id") long id) {
    public Flux<List<ProductResponseDto>> getProductsASync() throws ExecutionException, InterruptedException {
        return asyncService.getAllProducts();
    }

    @GetMapping(ASYNC + "/products/{id}")
    public Mono<ProductResponseDto> getProductsASync(@PathVariable("id") long id) {
        return asyncService.getProductById(id);
    }

}
