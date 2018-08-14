package com.liujianhui.raft;

import com.liujianhui.raft.msg.LogEntry;
import com.liujianhui.raft.msg.RaftMessageRequest;
import com.liujianhui.raft.server.Client;
import com.liujianhui.raft.server.Endpoint;

/**
 * Created by liujianhui on 2018/8/14.
 */
public class ClientTest {
    public static void main(String[] args) throws Exception {

        Client client = new Client(2, new Endpoint("127.0.0.1", 8999));
        RaftMessageRequest raftMessageRequest = new RaftMessageRequest();
        raftMessageRequest.setLastLogTerm(1L);
        raftMessageRequest.setLastLogIndex(2L);
        raftMessageRequest.setCommitIndex(3L);
        raftMessageRequest.setLogEntries(new LogEntry[4]);
        for (int i = 0; i < 10; ++i) {
            client.send(raftMessageRequest);
            Thread.sleep(2000);
        }
        System.in.read();
    }
}
