package com.example.flyfishshop.model;

import com.example.flyfishshop.util.CommonAddGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties("handler")
public class ItemInCart {
    private Integer id;
    private Integer memberId;
    @NotNull(message = "商品编号不可为空",groups = {CommonAddGroup.class})
    private Integer goodId;
    @NotNull(message = "商品数量不可为空",groups = {CommonAddGroup.class})
    @Min(value = 1,message = "商品数量至少为1",groups = {CommonAddGroup.class})
    private Integer qty;

    private Good good;
}
