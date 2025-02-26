package com.example.flyfishshop.model;

import com.example.flyfishshop.util.CommonAddGroup;
import com.example.flyfishshop.util.CommonEditGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@ToString
@JsonIgnoreProperties("handler")
public class UserAddress {
    private Integer id;
    private Integer memberId;
    // 向上关联
    @NotNull(message = "请选择完整地址", groups = {CommonAddGroup.class,CommonEditGroup.class})
    private Integer streetId;
    @NotEmpty(message = "详细地址不可为空", groups = {CommonAddGroup.class,CommonEditGroup.class})
    private String detail;
    @NotEmpty(groups = {CommonAddGroup.class,CommonEditGroup.class},message = "手机号不可为空")
    @Length(min = 11, max = 11, message = "请输入合法的手机号", groups = {CommonAddGroup.class, CommonEditGroup.class})
    private String phone;
    @NotEmpty(message = "收件人不可为空", groups = {CommonAddGroup.class, CommonEditGroup.class})
    private String receiver;
    private Boolean isDefault;



    private Address address;
}
