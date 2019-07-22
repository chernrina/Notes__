package com.example.notes__.model;

public enum PriorityType {

    CHECK(0),
    ORDINARY(1),
    IMPORTANT(2);

    private int index;

    PriorityType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
