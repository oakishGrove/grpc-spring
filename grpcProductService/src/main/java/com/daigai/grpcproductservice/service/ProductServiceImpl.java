package com.daigai.grpcproductservice.service;

import com.daigai.Empty;
import com.daigai.ProductRequest;
import com.daigai.ProductResponse;
import com.daigai.ProductServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
//import org.lognet.springboot.grpc.GRpcService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import net.devh.boot.grpc.server.service.GrpcService;
//import com.spring.grpc;

@AllArgsConstructor
@GrpcService
public class ProductServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {



    public void getProductById(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        long id = request.getId();
        System.out.println("\n\nIMAGINE THAT DATA WAS FETCHED: "+id);

        ProductResponse out = ProductResponse.newBuilder()
                .setId(1)
                .setName("dummy name")
                .setPrize(13.3f)
                .build();

        responseObserver.onNext(out);
        responseObserver.onCompleted();
    }

    public void getAllProducts(Empty request, StreamObserver<ProductResponse> responseObserver) {
        List<Long> mlist = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        for (long elem : mlist) {
            ProductResponse out = ProductResponse.newBuilder()
                    .setId(elem)
                    .setName("Name + " + elem)
                    .setPrize(elem * 1.3f)
                    .build();
            responseObserver.onNext(out);
        }
        responseObserver.onCompleted();
    }
}
