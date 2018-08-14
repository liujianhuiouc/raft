package com.liujianhui.raft.msg;

/**
 * Created by liujianhui on 2018/8/14.
 */
public class LogEntry {
    private int term;

    private LogValueType vaueType;

    private byte[] value;

    public LogEntry(int term, LogValueType vaueType, byte[] value) {
        this.term = term;
        this.vaueType = vaueType;
        this.value = value;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public LogValueType getVaueType() {
        return vaueType;
    }

    public void setVaueType(LogValueType vaueType) {
        this.vaueType = vaueType;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
