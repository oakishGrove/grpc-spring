package com.daigai.gateway.service;

import com.daigai.Empty;
import com.daigai.ProductRequest;
import com.daigai.ProductResponse;
import com.daigai.ProductServiceGrpc;
import com.daigai.gateway.dto.ProductResponseDto;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ProductGrpcAsyncService {

    @GrpcClient("grpcProductService")
    private ProductServiceGrpc.ProductServiceStub stub;

    public Flux<List<ProductResponseDto>> getAllProducts() throws ExecutionException, InterruptedException {


        CompletableFuture<ArrayList<ProductResponseDto>> completableFuture = new CompletableFuture<>();
        OutputStreamingResponse outputStreamingResponse = new OutputStreamingResponse(completableFuture);

        stub.getAllProducts(Empty.newBuilder().build(), outputStreamingResponse);

        return Flux.just(completableFuture.get());
//        Mono<ProductResponseDto> mono = Mono.fromFuture(completableFuture);
//        return completableFuture.fromFuture(completableFuture);

//
//        var products = this.stub.getProductById();
//        var allProducts = this.stub.getAllProducts(Empty.newBuilder().build());
//
//                getAllProducts(Empty.newBuilder().build());
//
//        List<ProductResponseDto> ret = new ArrayList<ProductResponseDto>();
//
//        ProductResponse elem = allProducts.next();
//        if (elem.isInitialized()) {
//
//            for (;allProducts.hasNext(); elem = allProducts.next()) {
//                ret.add(ProductResponseDto.builder()
//                        .id(elem.getId())
//                        .name(elem.getName())
//                        .prize(elem.getPrize())
//                        .build());
//            }
//        }
//        return ret;
    }

    public Mono<ProductResponseDto> getProductById(long id) {

        ProductRequest requestId = ProductRequest.newBuilder()
                .setId(id)
                .build();

        CompletableFuture<ProductResponseDto> completableFuture = new CompletableFuture<>();
        OutputMonoResponse outputMonoResponse = new OutputMonoResponse(completableFuture);

        stub.getProductById(requestId, outputMonoResponse);

//        Mono<ProductResponseDto> mono = Mono.fromFuture(completableFuture);
        return Mono.fromFuture(completableFuture);
    }


    private class OutputMonoResponse implements StreamObserver<ProductResponse> {

        private ProductResponseDto response = null;
        private final CompletableFuture<ProductResponseDto> completableFuture;

        private OutputMonoResponse(CompletableFuture<ProductResponseDto> completableFuture) {
            this.completableFuture = completableFuture;
        }

        @Override
        public void onNext(ProductResponse productResponse) {
            this.response = ProductResponseDto.builder()
                            .id(productResponse.getId())
                            .name(productResponse.getName())
                            .prize(productResponse.getPrize())
                            .build();
        }

        @Override
        public void onError(Throwable throwable) {
            this.completableFuture.cancel(true);
        }

        @Override
        public void onCompleted() {
            this.completableFuture.complete(this.response);
        }
    }

    private class OutputStreamingResponse implements StreamObserver<ProductResponse> {

        private ArrayList<ProductResponseDto> response = new ArrayList();
        private final CompletableFuture<ArrayList<ProductResponseDto>> completableFuture;

        private OutputStreamingResponse(CompletableFuture<ArrayList<ProductResponseDto>> completableFuture) {
            this.completableFuture = completableFuture;
        }

        @Override
        public void onNext(ProductResponse productResponse) {
            response.add(
                    ProductResponseDto.builder()
                        .id(productResponse.getId())
                        .name(productResponse.getName())
                        .prize(productResponse.getPrize())
                        .build());
        }

        @Override
        public void onError(Throwable throwable) {
            completableFuture.cancel(true);
        }

        @Override
        public void onCompleted() {
            completableFuture.complete(this.response);
        }
    }
}
