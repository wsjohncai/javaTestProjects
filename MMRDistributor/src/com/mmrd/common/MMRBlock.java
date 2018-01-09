package com.mmrd.common;

public class MMRBlock {
    private int start_addr;
    private int length;

    public MMRBlock(int start_addr, int length) {
        this.start_addr = start_addr;
        this.length = length;
    }

    public int getStart_addr() {
        return start_addr;
    }

    public void setStart_addr(int start_addr) {
        this.start_addr = start_addr;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

}
