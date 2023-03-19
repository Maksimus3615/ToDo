package com.shpp.models;

public enum Status {
    PLANNED(0),
    WORK_IN_PROGRESS(1),
    DONE(2),
    CANCELLED(3);

    private final int index;
    Status(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
