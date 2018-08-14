package com.liujianhui.raft.msg;

/**
 * Created by liujianhui on 2018/8/14.
 */
public enum LogValueType {


    /**
     * Log value for application, which means the value could only be understood by the application (not jraft)
     */
    Application {
        @Override
        public byte toByte() {
            return 1;
        }
    },

    /**
     * Log value is cluster configuration data
     */
    Configuration {
        @Override
        public byte toByte() {
            return 2;
        }
    },

    /**
     * Log value is cluster server id
     */
    ClusterServer {
        @Override
        public byte toByte() {
            return 3;
        }
    },

    /**
     * Log value is a pack of many log entries, this is used when a server is left far behind or a new server just join the cluster
     */
    LogPack {
        @Override
        public byte toByte() {
            return 4;
        }
    },

    /**
     * Log value is snapshot sync request data
     */
    SnapshotSyncRequest {
        @Override
        public byte toByte() {
            return 5;
        }
    };

    /**
     * Converts a LogValueType to a byte value
     *
     * @return byte value of the LogValueType
     */
    public abstract byte toByte();

    /**
     * Converts a byte value to LogValueType
     *
     * @param b byte value
     * @return LogValueType
     */
    public static LogValueType fromByte(byte b) {
        switch (b) {
            case 1:
                return Application;
            case 2:
                return Configuration;
            case 3:
                return ClusterServer;
            case 4:
                return LogPack;
            case 5:
                return SnapshotSyncRequest;
            default:
                throw new IllegalArgumentException(String.format("%d is not defined for LogValueType", b));
        }
    }
}

