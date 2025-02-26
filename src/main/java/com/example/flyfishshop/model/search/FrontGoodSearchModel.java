package com.example.flyfishshop.model.search;

import com.example.flyfishshop.model.Good;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FrontGoodSearchModel extends Good {
    String keyword;
}
