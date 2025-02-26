package com.example.flyfishshop.model;

public enum OrderState {
    NOT_PAY("未支付"), PAYED("已支付"),
    SHIPPED("已发货"), RECEIVED("已收货"),
    REFUNDING("退款中"), REFUNDED("已退款");
    private final String name;

    OrderState(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
