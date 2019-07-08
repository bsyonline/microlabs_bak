/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.service;

import com.rolex.microlabs.grpc.ProductRequest;
import com.rolex.microlabs.grpc.ProductResponse;
import com.rolex.microlabs.grpc.ProductServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;

/**
 * @author rolex
 * @since 2019
 */
@GrpcService(ProductServiceGrpc.class)
public class ProductService extends ProductServiceGrpc.ProductServiceImplBase {

    @Override
    public void findProductByClientId(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        ProductResponse helloResponse = ProductResponse.newBuilder()
                .setUserId(request.getUserId())
                .setProductId(11)
                .setProductName("grpc")
                .build();
        responseObserver.onNext(helloResponse);
        responseObserver.onCompleted();
    }
}
