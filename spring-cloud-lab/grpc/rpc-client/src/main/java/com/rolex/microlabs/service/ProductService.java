/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.service;

import com.rolex.microlabs.grpc.ProductRequest;
import com.rolex.microlabs.grpc.ProductResponse;
import com.rolex.microlabs.grpc.ProductServiceGrpc;
import io.grpc.Channel;
import net.devh.springboot.autoconfigure.grpc.client.GrpcClient;
import org.springframework.stereotype.Component;

/**
 * @author rolex
 * @since 2019
 */
@Component
public class ProductService {

    @GrpcClient("rpc-server")
    Channel serverChannel;

    public ProductResponse findProductByCustomerId(String customerId) {
        ProductServiceGrpc.ProductServiceBlockingStub stub = ProductServiceGrpc.newBlockingStub(serverChannel);
        ProductResponse response = stub.findProductByClientId(ProductRequest.newBuilder().setUserId(customerId).build());
        return response;
    }

}
