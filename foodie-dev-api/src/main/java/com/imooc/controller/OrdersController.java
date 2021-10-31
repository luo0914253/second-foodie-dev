package com.imooc.controller;

import com.imooc.enums.PayMethod;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.service.ItemService;
import com.imooc.service.OrderService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "订单相关",tags = {"订单相关的api接口"})
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "用户下单",notes = "用户下单",httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO submitOrderBO){

        if (!submitOrderBO.getPayMethod().equals(PayMethod.WEIXIN.type)
                ||!submitOrderBO.getPayMethod().equals(PayMethod.ZHIFUBAO.type)){
            return IMOOCJSONResult.errorMsg("支付方式不支持");
        }
//      创建订单
        orderService.createOrder(submitOrderBO);
//      清空购物车已结算商品
//      向支付中心发送订单
        return IMOOCJSONResult.ok();
    }
}
