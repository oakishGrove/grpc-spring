package com.daigai.gateway.service;

import com.daigai.*;
import com.daigai.gateway.dto.ProductResponseDto;
import org.springframework.stereotype.Service;

import net.devh.boot.grpc.client.inject.GrpcClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductGrpcSyncService {

    @GrpcClient("grpcProductService")
    private ProductServiceGrpc.ProductServiceBlockingStub blockingStub;
//    private ProductServiceGrpc.ProductServiceRpcBlockingStub blockingStub;

//    @GrpcClient("grpcProductService")
//    private ProductServiceGrpc.ProductServiceStub asyncStub;

    public List<ProductResponseDto> getProducts() {
        var allProducts = this.blockingStub.getAllProducts(Empty.newBuilder().build());

        List<ProductResponseDto> ret = new ArrayList<ProductResponseDto>();

        ProductResponse elem = allProducts.next();
        if (elem.isInitialized()) {

            for (;allProducts.hasNext(); elem = allProducts.next()) {
                ret.add(ProductResponseDto.builder()
                        .id(elem.getId())
                        .name(elem.getName())
                        .prize(elem.getPrize())
                        .build());
            }
        }

        return ret;
    }

    public ProductResponseDto getProductById(long id) {
        ProductRequest requestId = ProductRequest.newBuilder()
                .setId(id)
                .build();

        ProductResponse response = blockingStub.getProductById(requestId);
        return ProductResponseDto.builder()
                .name(response.getName())
                .id(response.getId())
                .prize(response.getPrize())
                .build();
    }
}
