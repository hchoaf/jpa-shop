package com.hchoaf.jpashop.repository;

import com.hchoaf.jpashop.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;
}
