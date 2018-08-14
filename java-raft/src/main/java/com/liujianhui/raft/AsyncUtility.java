package com.liujianhui.raft;

import java.nio.channels.CompletionHandler;
import java.util.function.BiConsumer;

/**
 * Created by liujianhui on 2018/8/14.
 */
public class AsyncUtility {
    public static <V, A> CompletionHandler<V, A> handlerFrom(BiConsumer<V, A> completed, BiConsumer<Throwable, A> failed) {
        return new CompletionHandler<V, A>() {
            @Override
            public void completed(V result, A attachment) {
                completed.accept(result, attachment);
            }

            @Override
            public void failed(Throwable exc, A attachment) {
                failed.accept(exc, attachment);
            }
        };
    }
}
