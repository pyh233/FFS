package com.example.flyfishshop.model;

import com.example.flyfishshop.util.validate.CommonEditGroup;
import com.example.flyfishshop.util.validate.OrderDataPatch;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Setter
@Getter
@ToString
@JsonIgnoreProperties("handler")
public class Order {
    private Integer id;
    private String orderNo;
    private Integer memberId;
    private OrderState state;
    private LocalDateTime createdTime;

    @Min(value = 0,groups = OrderDataPatch.class)
    @NotNull
    private BigDecimal totalPay;
    private String note;
    // 一个订单对应一个地址
    //  不能这么写，如果用户把地址删了级联查询就找不到了
    //  private Address address;
    // 一个订单对应一个地址，可以直接存在order中，可以减少表的数量
    @NotEmpty(message = "收货人不可为空", groups = {OrderDataPatch.class, CommonEditGroup.class})
    private String receiver;
    @NotEmpty(message = "手机号不可为空",groups = {OrderDataPatch.class, CommonEditGroup.class})
    @Length(min = 11, max = 11,message = "请输入合法的手机号",groups = {OrderDataPatch.class, CommonEditGroup.class})
    private String receiverPhone;
    @NotEmpty(message = "收货地址不可为空", groups = {OrderDataPatch.class, CommonEditGroup.class})
    private String receiverDetailAddress;

    private String payType;
    private LocalDateTime payTime;

    // 发货时间
    private LocalDateTime putTime;
    // 收货时间
    private LocalDateTime confirmTime;
    // 申请退款时间
    private LocalDateTime applyRefundTime;
    // 确认接受退款时间
    private LocalDateTime agreeRefundTime;
    // 退款原因
    private String refundCause;
    // 接受退款商家信息



    // 不可以使用Goods或者ItemInCart,其中
    // 前者会因为商品下架而导致找不到商品信息，后者会因为创建订单以后删除购物车中商品
    // private List<ItemInCart> items;
    // 但是因为一个订单可能包含多个商品，所以没法放在这里面，只能单独创建一个实例
    private List<ItemInOrder>  orderItemList;
}
