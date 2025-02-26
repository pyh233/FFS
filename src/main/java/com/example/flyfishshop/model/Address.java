package com.example.flyfishshop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

@Getter
@Setter
@JsonIgnoreProperties("handler")
public class Address {
    private Integer id;
    private Integer parentId;
    private String name;
    private Address parent;

    // address下拉列表使用
    private String street;
    private String area;
    private String city;
    private String province;
    private String country;


    public String getAddressStr() {
        if (parent == null) {
            return "" + name;
        }
        return parent.getAddressStr() + " " + name;
    }
}
