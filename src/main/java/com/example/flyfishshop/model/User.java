package com.example.flyfishshop.model;

import com.example.flyfishshop.util.validate.CommonAddGroup;
import com.example.flyfishshop.util.validate.CommonEditGroup;
import com.example.flyfishshop.util.validate.CommonLoginGroup;
import com.example.flyfishshop.util.validate.CommonRegisterGroup;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class User {
    Integer id;
    @Length(min = 3, max = 16, message = "账号长度必须介于3~16之间", groups = {CommonAddGroup.class, CommonRegisterGroup.class})
    @NotEmpty(message = "账号不可为空", groups = {CommonLoginGroup.class, CommonAddGroup.class, CommonRegisterGroup.class})
    String account;
    @Length(min = 3, max = 16, message = "密码长度必须介于6~16之间", groups = {CommonAddGroup.class, CommonRegisterGroup.class})
    @NotEmpty(message = "密码不可为空", groups = {CommonLoginGroup.class, CommonAddGroup.class, CommonRegisterGroup.class})
    String password;
    String name;
    String sex;
    LocalDate birthday;
    @NotEmpty(message = "手机号不可为空", groups = {CommonAddGroup.class, CommonRegisterGroup.class})
    @Length(min = 11, max = 11, message = "请输入合法的手机号", groups = {CommonAddGroup.class, CommonEditGroup.class, CommonRegisterGroup.class})
    String phone;
    @Email(message = "请输入合法的邮箱", groups = {CommonAddGroup.class, CommonEditGroup.class,CommonRegisterGroup.class})
    String email;
    @Length(min = 18, max = 18, message = "请输入合法的身份证号", groups = {CommonAddGroup.class, CommonEditGroup.class,CommonRegisterGroup.class})
    String cardId;
    String description;
    String avatar;
    LocalDateTime createdTime;
    String createdBy;
    LocalDateTime updatedTime;
    String updatedBy;


    String qq;
    String wechat;
}
