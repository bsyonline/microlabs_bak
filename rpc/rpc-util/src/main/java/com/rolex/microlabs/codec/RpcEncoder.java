/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.codec;

import com.rolex.microlabs.util.SerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author rolex
 * @since 2019
 */
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            byte[] data = SerializationUtils.serialize(in, genericClass);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}