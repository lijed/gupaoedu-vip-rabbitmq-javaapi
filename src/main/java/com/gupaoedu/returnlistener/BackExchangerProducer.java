/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.gupaoedu.returnlistener;

import com.gupaoedu.util.ResourceUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @date 2021/2/24 23:13
 * Project Name: gupaoedu-vip-rabbitmq-javaapi
 */
public class BackExchangerProducer {

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(ResourceUtil.getKey("rabbitmq.uri"));

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().deliveryMode(2).
                contentEncoding("UTF-8").build();


        //备份交换机
        channel.exchangeDeclare("ALTERNATE_EXCHANGE", "topic", false, false, false, null);
        channel.queueDeclare("ALTERNATE_QUEUE", false, false, false, null);
        channel.queueBind("ALTERNATE_QUEUE", "ALTERNATE_EXCHANGE", "#");

        // 在声明交换机的时候指定备份交换机
        Map<String,Object> arguments = new HashMap<String,Object>();
        arguments.put("alternate-exchange","ALTERNATE_EXCHANGE");
        channel.exchangeDeclare("TEST_EXCHANGE","topic", false, false, false, arguments);

        // 发送到了默认的交换机上，由于没有任何队列使用这个关键字跟交换机绑定，所以会被退回
        // 第三个参数是设置的mandatory，如果mandatory是false，消息也会被直接丢弃
        channel.basicPublish("TEST_EXCHANGE", "gupaodirect", false, properties, "只为更好的你".getBytes());

        TimeUnit.SECONDS.sleep(10);

        channel.close();
        connection.close();
    }
}
