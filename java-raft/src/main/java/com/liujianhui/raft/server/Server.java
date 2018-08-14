package com.liujianhui.raft.server;

import com.liujianhui.raft.AsyncUtility;
import com.liujianhui.raft.msg.RaftMessageRequest;
import com.liujianhui.raft.utils.ParseUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liujianhui on 2018/8/14.
 */
public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class);

    private AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public Server(int threadPoolSize, Endpoint endpoint) {
        try {
            AsynchronousChannelGroup asynchronousChannelGroup = AsynchronousChannelGroup.
                    withFixedThreadPool(threadPoolSize, new DefaultThreadFactory());
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open(asynchronousChannelGroup).
                    bind(new InetSocketAddress(endpoint.getHost(), endpoint.getPort()));
        } catch (IOException e) {
            LOGGER.error("fail to create asynchronous channel group", e);
        }

    }

    public void startSynchronous() {
        while (true) {
            try {
                Future<AsynchronousSocketChannel> asynchronousSocketChannelFuture = asynchronousServerSocketChannel.accept();
                AsynchronousSocketChannel asc = asynchronousSocketChannelFuture.get();
                LOGGER.info("get connection from remote address " + asc.getRemoteAddress().toString());
                ByteBuffer byteBuffer = ByteBuffer.allocate(4);
                Future<Integer> byteReads = asc.read(byteBuffer);
                LOGGER.info("readed byte size is " + byteReads.get());
                LOGGER.info("result from out: " + ByteBuffer.wrap(byteBuffer.array()).get());
            } catch (InterruptedException e) {
                LOGGER.warn("socket channel interrupted by other operation", e);
            } catch (ExecutionException e) {
                LOGGER.warn("execution exception occur while get the channel", e);
            } catch (IOException e) {
                LOGGER.warn("IO exception occur while get the channel", e);
            } catch (Exception e) {
                LOGGER.warn("unchecked exception occur while get the channel", e);
            }
        }

    }

    public void startAsynchoronous(RaftMessageHandler raftMessageHandler) {
        try {
            accept(raftMessageHandler);
        } catch (Exception e) {
            LOGGER.warn("unchecked exception occur while get the channel", e);
        }
    }

    private void accept(RaftMessageHandler raftMessageHandler) {
        try {
            asynchronousServerSocketChannel.accept(raftMessageHandler, AsyncUtility.handlerFrom((
                    AsynchronousSocketChannel asynchronousSocketChannel, RaftMessageHandler handler) -> {
                try {
                    accept(raftMessageHandler);
                    LOGGER.info("connection from remote address " + asynchronousSocketChannel.getRemoteAddress().toString());
                    //asynchronousSocketChannel.close();
                    processRaftMessageRequest(asynchronousSocketChannel, handler);
                } catch (Throwable e) {
                    LOGGER.error("error occur", e);
                }

            }, (Throwable e, RaftMessageHandler handler) -> {
                LOGGER.error("error occur", e);
                accept(raftMessageHandler);
            }));
        } catch (Exception e) {
            LOGGER.error("error occur in accepting", e);
            accept(raftMessageHandler);
        }

        LOGGER.info("accept over!");
    }


    private void processRaftMessageRequest(AsynchronousSocketChannel asynchronousSocketChannel,
                                           RaftMessageHandler raftMessageHandler) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(RaftMessageRequest.RAFT_MESSAGE_REQUEST_SIZE);
        readRaftMessageRequest(byteBuffer, asynchronousSocketChannel, raftMessageHandler);

    }

    private void readRaftMessageRequest(ByteBuffer byteBuffer, AsynchronousSocketChannel asynchronousSocketChannel,
                                        RaftMessageHandler raftMessageHandler) {
        asynchronousSocketChannel.read(byteBuffer, raftMessageHandler,
                AsyncUtility.handlerFrom((Integer readBytes, RaftMessageHandler handler) -> {
                    if (readBytes == -1 && byteBuffer.hasRemaining()) {
                        LOGGER.warn("misssing required bytes");
                        return;
                    }

                    if (!byteBuffer.hasRemaining()) {
                        RaftMessageRequest raftMessageRequest = ParseUtils.parseToRaftMessageRequest(byteBuffer);
                        LOGGER.info("got a raft message request " + String.valueOf(raftMessageRequest));
                        LOGGER.info("start to read another raft message from channel");
                        processRaftMessageRequest(asynchronousSocketChannel, handler);
                        return;
                    }

                    LOGGER.info("still read bytes from channel");
                    readRaftMessageRequest(byteBuffer, asynchronousSocketChannel, raftMessageHandler);
                }, (Throwable error, RaftMessageHandler handler) -> {
                    LOGGER.error("fail to read bytes from asynchronous socket channel", error);
                }));
    }


    private static class DefaultThreadFactory implements ThreadFactory {
        private AtomicInteger index = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("server-" + index.getAndIncrement());
            return thread;
        }
    }
}

