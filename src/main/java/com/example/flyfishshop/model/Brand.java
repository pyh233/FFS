package com.example.flyfishshop.model;

import com.example.flyfishshop.util.CommonAddGroup;
import com.example.flyfishshop.util.CommonEditGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Brand {
    private Integer id;
    @NotEmpty(message="品牌名不可为空",groups = {CommonEditGroup.class, CommonAddGroup.class})
    private String name;
    private String company;
    private String logo;
    private String site;
    private String description;
}
