package com.example.notes__.model;

public enum PriorityType {

    ORDINARY(0),
    IMPORTANT(1);

    private int index;

    PriorityType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
