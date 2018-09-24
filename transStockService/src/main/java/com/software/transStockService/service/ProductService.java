package com.software.transStockService.service;

import com.software.transOrderService.model.Event;

public interface ProductService {
    /**
     * 处理订单创建成功消息
     * @param event
     */
    void handleOrderSuccessMsg(Event event);
}
