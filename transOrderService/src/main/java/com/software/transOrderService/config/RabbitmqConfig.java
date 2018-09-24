package com.software.transOrderService.config;

import com.software.transOrderService.constants.CommonConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    @Bean
    public Queue successQueue(){
        return new Queue(CommonConstant.SUCCESS_QUEUE);
    }

    @Bean
    public Queue failureQueue(){
        return new Queue(CommonConstant.FAILURE_QUEUE);
    }
}
