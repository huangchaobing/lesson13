package com.software.transOrderService.service;

import com.software.transOrderService.model.Event;
import com.software.transOrderService.model.Order;

public interface OrderService {
    /**
     * 创建订单
     * @param productId
     * @param count
     * @return
     */
    Order createOrder(Integer productId,Integer count);

    /**
     * 处理库存信息消息
     * @param event
     */
    void handleStockFailureMsg(Event event);
}
