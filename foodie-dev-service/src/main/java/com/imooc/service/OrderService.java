package com.imooc.service;

import com.imooc.pojo.bo.SubmitOrderBO;

public interface OrderService {
    /**
     * 创建订单
     * @param submitOrderBO
     */
    void createOrder(SubmitOrderBO submitOrderBO);
}
