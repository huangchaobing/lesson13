package com.software.transOrderService.service.impl;

import com.software.transOrderService.constants.CommonConstant;
import com.software.transOrderService.model.Event;
import com.software.transOrderService.model.Order;
import com.software.transOrderService.repository.EventRepository;
import com.software.transOrderService.repository.OrderRepository;
import com.software.transOrderService.service.OrderService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service("orderService")
public class OrderServiceImpl implements OrderService{
    private static final Integer CREATE_ORDER_EVENT = 1;
    private static final Integer UPDATE_ORDER_EVENT = 2;
    private static final Integer DELETE_ORDER_EVENT = 3;

    @Autowired
    private OrderRepository orderDao;

    @Autowired
    private EventRepository eventDao;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    @Transactional
    public Order createOrder(Integer productId, Integer count) {
        Order order = new Order();

        try{

            order.setProductName("安踏运动鞋");
            order.setProductId(productId);
            order.setCreatedDate(new Date());
            order.setCount(count);

            //保存订单
            order = orderDao.save(order);
        }
        finally {
            Event event = new Event();

            event = createEvent(productId, count, order, event);

            //向消息队列发送事件消息
            rabbitTemplate.convertAndSend(CommonConstant.SUCCESS_QUEUE, event);
        }

        return order;
    }

    private Event createEvent(Integer productId, Integer count, Order order, Event event) {
        event.setEventType(CREATE_ORDER_EVENT);
        event.setModelName("订单");
        event.setModelId(order.getOrderId());
        event.setCreatedDate(new Date());
        event.setOrderCount(count);
        event.setProductId(productId);

        //保存事件信息
        event = eventDao.save(event);
        return event;
    }

    @RabbitListener(queues = CommonConstant.FAILURE_QUEUE)
    @Override
    @Transactional
    public void handleStockFailureMsg(Event event) {
        Integer eventId = event.getEventId();

        Event msgEvent = eventDao.findOne(eventId);

        if(msgEvent != null){
            Integer orderId = eventDao.findOne(eventId).getModelId();

            //删除订单信息
            orderDao.delete(orderId);
        }
    }
}
