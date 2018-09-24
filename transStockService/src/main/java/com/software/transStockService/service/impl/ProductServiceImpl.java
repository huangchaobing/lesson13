package com.software.transStockService.service.impl;

import com.software.transOrderService.model.Event;
import com.software.transStockService.constants.CommonConstant;
import com.software.transStockService.model.Product;
import com.software.transStockService.repository.ProductRepository;
import com.software.transStockService.service.ProductService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("productService")
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productDao;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    @RabbitListener(queues = CommonConstant.SUCCESS_QUEUE)
    public void handleOrderSuccessMsg(Event event) {
        Integer productId = event.getProductId();

        Product product = productDao.findOne(productId);

        if (product.getStockCount() >= event.getOrderCount()) {
            //正常扣减库存
            product.setStockCount(product.getStockCount() - event.getOrderCount());

            productDao.saveAndFlush(product);
        } else {
            //回滚生产订单信息
            rabbitTemplate.convertAndSend(CommonConstant.FAILURE_QUEUE,event);
        }
    }
}
