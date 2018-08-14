package com.liujianhui.raft.server;

import com.liujianhui.raft.msg.RaftMessageRequest;
import com.liujianhui.raft.msg.RaftMessageResponse;

/**
 * Created by liujianhui on 2018/8/14.
 */
public interface RaftMessageHandler {
    public RaftMessageResponse process(RaftMessageRequest raftMessageRequest);
}
