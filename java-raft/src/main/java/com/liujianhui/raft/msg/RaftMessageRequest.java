package com.liujianhui.raft.msg;

import java.util.Arrays;

/**
 * Created by liujianhui on 2018/8/14.
 */
public class RaftMessageRequest {
    public static final int RAFT_MESSAGE_REQUEST_SIZE = Long.BYTES * 3 + Integer.BYTES;
    private long lastLogTerm;
    private long lastLogIndex;
    private long commitIndex;
    private LogEntry[] logEntries;

    public long getLastLogTerm() {
        return lastLogTerm;
    }

    public void setLastLogTerm(long lastLogTerm) {
        this.lastLogTerm = lastLogTerm;
    }

    public long getLastLogIndex() {
        return lastLogIndex;
    }

    public void setLastLogIndex(long lastLogIndex) {
        this.lastLogIndex = lastLogIndex;
    }

    public long getCommitIndex() {
        return commitIndex;
    }

    public void setCommitIndex(long commitIndex) {
        this.commitIndex = commitIndex;
    }

    public LogEntry[] getLogEntries() {
        return logEntries;
    }

    public void setLogEntries(LogEntry[] logEntries) {
        this.logEntries = logEntries;
    }

    @Override
    public String toString() {
        return "RaftMessageRequest{" +
                "lastLogTerm=" + lastLogTerm +
                ", lastLogIndex=" + lastLogIndex +
                ", commitIndex=" + commitIndex +
                ", logEntries=" + Arrays.toString(logEntries) +
                '}';
    }
}
