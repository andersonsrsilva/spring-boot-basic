package com.basic.domain.enums;

public enum Number {

    ONE(1),
    TWO(2),
    TREE(3),
    FOUR(4),
    SIXTY(60),
    TOTAL_MONTH(168);

    private int value;

    Number(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

}
