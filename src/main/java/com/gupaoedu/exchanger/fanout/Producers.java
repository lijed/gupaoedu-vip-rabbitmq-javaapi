/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.gupaoedu.exchanger.fanout;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import sun.util.calendar.BaseCalendar;

import java.util.Date;


/**
 * @author Administrator
 * @date 2021/2/23 22:52
 * Project Name: gupaoedu-vip-rabbitmq-javaapi
 */
public class Producers {
        private final static String EXCHANGE_NAME = "amq.fanout";

        public static void main(String[] args) throws Exception {
            ConnectionFactory factory = new ConnectionFactory();
            // 连接IP
            factory.setHost("127.0.0.1");
            // 连接端口
            factory.setPort(5672);
            // 虚拟机
            factory.setVirtualHost("vh_customer");
            // 用户
            factory.setUsername("guest");
            factory.setPassword("guest");

            // 建立连接
            Connection conn = factory.newConnection();
            // 创建消息通道
            Channel channel = conn.createChannel();

            // 发送消息
            String msg = "Hello world, this is a fanout messsage";

            for (int i = 0; i < 100; i++) {
                // String exchange, String routingKey, BasicProperties props, byte[] body
                AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().messageId("jed").timestamp(new Date()).build();
                channel.basicPublish(EXCHANGE_NAME, "", properties, msg.getBytes());
            }
            channel.close();
            conn.close();
        }
}
