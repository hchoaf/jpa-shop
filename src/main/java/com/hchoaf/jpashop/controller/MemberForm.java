package com.hchoaf.jpashop.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "You need to enter name")
    private String name;
    private String city;
    private String street;
    private String zipcode;
}
