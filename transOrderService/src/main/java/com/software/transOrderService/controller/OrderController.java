package com.software.transOrderService.controller;

import com.software.transOrderService.model.Order;
import com.software.transOrderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("/createOrder")
    public Order createOrder(Integer productId,Integer count){
        if(!StringUtils.isEmpty(productId) &&
                (count != null && count > 0)){
            return orderService.createOrder(productId,count);
        }

        return null;
    }
}
