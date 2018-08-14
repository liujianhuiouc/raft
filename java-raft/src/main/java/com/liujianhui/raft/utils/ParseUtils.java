package com.liujianhui.raft.utils;

import com.liujianhui.raft.msg.LogEntry;
import com.liujianhui.raft.msg.RaftMessageRequest;

import java.nio.ByteBuffer;

/**
 * Created by liujianhui on 2018/8/14.
 */
public final class ParseUtils {
    public static final RaftMessageRequest parseToRaftMessageRequest(ByteBuffer byteBuffer) {
        byteBuffer = ByteBuffer.wrap(byteBuffer.array());
        RaftMessageRequest raftMessageRequest = new RaftMessageRequest();
        raftMessageRequest.setCommitIndex(byteBuffer.getLong());
        raftMessageRequest.setLastLogIndex(byteBuffer.getLong());
        raftMessageRequest.setLastLogTerm(byteBuffer.getLong());
        raftMessageRequest.setLogEntries(new LogEntry[byteBuffer.getInt()]);
        return raftMessageRequest;
    }

    public static final ByteBuffer serialMessage(RaftMessageRequest raftMessageRequest) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(RaftMessageRequest.RAFT_MESSAGE_REQUEST_SIZE);
        byteBuffer.putLong(raftMessageRequest.getCommitIndex());
        byteBuffer.putLong(raftMessageRequest.getLastLogIndex());
        byteBuffer.putLong(raftMessageRequest.getLastLogTerm());
        byteBuffer.putInt(raftMessageRequest.getLogEntries().length);
        return ByteBuffer.wrap(byteBuffer.array());
    }
}
