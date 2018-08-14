package com.liujianhui.raft;

import com.liujianhui.raft.msg.RaftMessageRequest;
import com.liujianhui.raft.server.Endpoint;
import com.liujianhui.raft.server.Server;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");

        Endpoint endpoint = new Endpoint("127.0.0.1", 8999);

        Server server = new Server(2, endpoint);
        server.startAsynchoronous((RaftMessageRequest request) -> {
            return null;
        });
        System.in.read();
    }
}
