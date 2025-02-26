package com.example.flyfishshop.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
@Getter
@Setter
@ToString
public class ItemInOrder {
    private Integer id;
    private Integer orderId;
    private Integer goodId;
    private BigDecimal price;
    private Integer count;


    // 必须把商品信息存在这里，与订单有关的所有信息都不能依赖其他表进行关联查询
    private String productName;
    private String productPic;
    private String productSummary;
}
