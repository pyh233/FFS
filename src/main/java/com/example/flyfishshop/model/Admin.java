package com.example.flyfishshop.model;

import com.example.flyfishshop.util.CommonAddGroup;
import com.example.flyfishshop.util.CommonEditGroup;
import com.example.flyfishshop.util.CommonLoginGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class Admin {
    Integer id;
    @NotEmpty(message = "管理员账户不能为空", groups = {CommonLoginGroup.class, CommonAddGroup.class})
    @Length(min = 5, max = 20, message = "管理员账户长度必须在5-20之间", groups = {CommonAddGroup.class})
    String account;
    @NotEmpty(message = "密码不可为空", groups = {CommonLoginGroup.class})
    @Length(min = 6, max = 20, message = "管理员密码长度必须在6-20之间", groups = {CommonAddGroup.class})
    String password;
    String avatarUrl;
    String description;

    LocalDate lastLoginTime;
    String lastLoginIp;
    LocalDateTime createTime;
    String createdBy;
    LocalDateTime lastModifyTime;
    String lastModifiedBy;
}
