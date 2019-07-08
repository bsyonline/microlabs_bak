/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.util;

import com.google.common.base.Preconditions;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rolex
 * @since 2018
 */
public class ZooKeeperUtils {

    static Logger logger = LoggerFactory.getLogger(ZooKeeperUtils.class);

    /**
     * Ensures the given {@code path} exists in the ZK cluster accessed by {@code zkClient}.  If the
     * path already exists, nothing is done; however if any portion of the path is missing, it will be
     * created as a persistent zookeeper node. The given {@code path} must be a valid zookeeper absolute
     * path.
     *
     * @param zkClient the client to use to access the ZK cluster
     * @param path     the path to ensure exists
     */
    public static void ensurePath(ZkClient zkClient, String path) {
        Preconditions.checkNotNull(zkClient);
        Preconditions.checkNotNull(path);
        Preconditions.checkArgument(path.startsWith("/"));

        ensurePathInternal(zkClient, path);
    }

    private static void ensurePathInternal(ZkClient zkClient, String path) {
        if (!zkClient.exists(path)) {
            logger.info("path {} not exists, and will be create.", path);
            int lastPathIndex = path.lastIndexOf('/');
            if (lastPathIndex > 0) {
                ensurePathInternal(zkClient, path.substring(0, lastPathIndex));
            }
            zkClient.create(path, null, CreateMode.PERSISTENT);
        }
    }
}
