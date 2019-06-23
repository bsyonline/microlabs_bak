/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.telnet;

/**
 * @author rolex
 * @since 2019
 */
public class NettyTelnetTest {
    public static void main(String[] args) throws InterruptedException {
        NettyTelnetServer server = new NettyTelnetServer();
        server.open();
    }
}
