package com.example.flyfishshop.model.search;

import com.example.flyfishshop.model.Order;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties("handler")
public class SearchOrderModel extends Order {
    private String account;
    private String createdDateRange;
}
