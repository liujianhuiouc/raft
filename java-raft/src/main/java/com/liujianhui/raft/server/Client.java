package com.liujianhui.raft.server;

import com.liujianhui.raft.AsyncUtility;
import com.liujianhui.raft.msg.RaftMessageRequest;
import com.liujianhui.raft.msg.RaftMessageResponse;
import com.liujianhui.raft.utils.ParseUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liujianhui on 2018/8/14.
 */
public class Client {
    public static final Logger LOGGER = Logger.getLogger(Client.class);

    private AsynchronousSocketChannel connection;

    private AsynchronousChannelGroup channelGroup;

    private Endpoint remote;

    public Client(int poolSize, Endpoint remote) {
        try {
            channelGroup = AsynchronousChannelGroup.withFixedThreadPool(poolSize, new DefaultThreadFactory());
        } catch (IOException e) {
            LOGGER.error("fail to create channel group", e);
        }
        this.remote = remote;
    }

    public CompletableFuture<RaftMessageResponse> send(RaftMessageRequest raftMessageRequest) throws Exception {
        if (connection == null || !connection.isOpen()) {
            this.connection = AsynchronousSocketChannel.open(this.channelGroup);
            this.connection.connect(new InetSocketAddress(remote.getHost(), remote.getPort()), raftMessageRequest,
                    AsyncUtility.handlerFrom((Void v, RaftMessageRequest message) -> {
                        sendMessage(message);
                    }, (Throwable e, RaftMessageRequest message) -> {
                        LOGGER.error("fail to send message", e);
                    }));
        } else {
            LOGGER.info("connection has already established");
            sendMessage(raftMessageRequest);
        }

        return null;
    }


    private void sendMessage(RaftMessageRequest raftMessageReques) {
        connection.write(ParseUtils.serialMessage(raftMessageReques), null, AsyncUtility.handlerFrom((Integer v, RaftMessageRequest message) -> {
            LOGGER.info("bytes send to server " + v);
        }, (Throwable e, RaftMessageRequest message) -> {
            LOGGER.error("fail to send message", e);
        }));
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private AtomicInteger index = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("client-" + index.getAndIncrement());
            return thread;
        }
    }
}

