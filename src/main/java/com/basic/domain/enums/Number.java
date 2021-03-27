package com.basic.domain.enums;

public enum Number {

    ONE(1),
    TWO(2),
    TREE(3);

    private int value;

    Number(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

}
