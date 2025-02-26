package com.example.flyfishshop.model;

import com.example.flyfishshop.util.CommonAddGroup;
import com.example.flyfishshop.util.CommonEditGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aspectj.bridge.IMessage;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@JsonIgnoreProperties("handler")
public class Good {
    @NotNull(message = "id不可为空",groups = {CommonEditGroup.class})
    private Integer id;
    @NotEmpty(message="商品编号不可为空",groups = {CommonEditGroup.class, CommonAddGroup.class})
    private String skuNo;
    @NotEmpty(message = "商品名称不可为空",groups = {CommonEditGroup.class, CommonAddGroup.class})
    private String name;
    private String alias;
    private String summary;
    @NotNull(message = "种类不可为空", groups = {CommonEditGroup.class, CommonAddGroup.class})
    private Integer categoryId;
    private Integer brandId;
    @Digits(message = "建议零售价不符合规定",integer = 10, fraction = 2,groups = {CommonEditGroup.class,CommonAddGroup.class})
    private double markPrice;
    @Digits(message = "零售价不符合规定",integer = 10, fraction = 2,groups = {CommonEditGroup.class,CommonAddGroup.class})
    private double price;
    @Min(message = "库存数不符合规定",groups = {CommonEditGroup.class,CommonAddGroup.class},value = 0)
    private Integer qty;
    @NotEmpty(message = "必须添加一张主要图片!",groups = {CommonEditGroup.class, CommonAddGroup.class})
    private String pic;
    private String otherPics;
    private String detail;
    private String description;
    private LocalDateTime createdTime;
    private String createdBy;
    private LocalDateTime updatedTime;
    private String updatedBy;
    // 使用类别树
    private Category category;
    private Brand brand;

}
