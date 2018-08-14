package com.liujianhui.raft.server;

/**
 * Created by liujianhui on 2018/8/14.
 */
public class Endpoint {
    private String host;

    private int port;

    public Endpoint(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
