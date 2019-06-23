/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.util;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rolex
 * @since 2019
 */
@Slf4j
public class SerializationUtils {
    public static <T> byte[] serialize(Object in, Class<T> tClass) {
        log.info("serialize {}", tClass);
        RuntimeSchema<T> schema = RuntimeSchema.createFrom(tClass);
        byte[] bytes = ProtostuffIOUtil.toByteArray((T) in, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        return bytes;
    }

    public static <T> Object deserialize(byte[] data, Class<T> tClass) {
        log.info("deserialize {}", tClass);
        RuntimeSchema<T> schema = RuntimeSchema.createFrom(tClass);
        T t = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, t, schema);
        return t;
    }
}
