package com.imooc.service.impl;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.*;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.ItemInfoVO;
import com.imooc.service.AddressService;
import com.imooc.service.ItemService;
import com.imooc.service.OrderService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private Sid sid;
    /**
     * 创建订单
     * @param submitOrderBO
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void createOrder(SubmitOrderBO submitOrderBO) {
//      1、保存订单信息
        Orders orders = new Orders();
        String orderId = sid.nextShort();
        orders.setId(orderId);
        orders.setUserId(submitOrderBO.getUserId());
        UserAddress userAddress = addressService.queryUserAddress(submitOrderBO.getUserId(),
                submitOrderBO.getAddressId());
        orders.setReceiverName(userAddress.getReceiver());
        orders.setReceiverMobile(userAddress.getMobile());
        orders.setReceiverAddress(userAddress.getProvince()+" "+userAddress.getCity()+" "+
                userAddress.getDistrict()+" "+userAddress.getDetail());
        Integer postAmount = 0;
        orders.setPostAmount(postAmount);
        orders.setPayMethod(submitOrderBO.getPayMethod());
        orders.setLeftMsg(submitOrderBO.getLeftMsg());
        Integer totalAmount = 0;//订单总价格
        Integer realPayAmount = 0;//实际支付总价格
//      2、根据itemSpecIds保存商品信息
        String[] itemSpecIds = submitOrderBO.getItemSpecIds().split(",");
        for (String itemSpecId : itemSpecIds) {
//          TODO 整合redis后，商品购买的数量重新从redis的购物车中获取
            int buyCounts = 1;
//          2.1、根据ids查找商品,计算价格
            ItemsSpec itemsSpec = itemService.queryItemSpecById(itemSpecId);
            realPayAmount += itemsSpec.getPriceDiscount()*buyCounts;//优惠价
            totalAmount += itemsSpec.getPriceNormal()*buyCounts;
//          2.2 查找商品其他属性，保存商品
            String itemId = itemsSpec.getItemId();
            Items items = itemService.queryItem(itemId);
            String imgUrl = itemService.queryItemMainImgById(itemId);

            OrderItems orderItems = new OrderItems();
            String orderItemId = sid.nextShort();
            orderItems.setId(orderItemId);
            orderItems.setOrderId(orderId);
            orderItems.setItemId(itemId);
            orderItems.setItemImg(imgUrl);
            orderItems.setItemName(items.getItemName());
            orderItems.setItemSpecId(itemSpecId);
            orderItems.setItemSpecName(itemsSpec.getName());
            orderItems.setPrice(itemsSpec.getPriceDiscount());
            orderItems.setBuyCounts(buyCounts);
            orderItemsMapper.insert(orderItems);
//          在用户提交订单以后，规格表中需要扣除库存
        }
        orders.setTotalAmount(totalAmount);
        orders.setRealPayAmount(realPayAmount);
        orders.setCreatedTime(new Date());
        orders.setUpdatedTime(new Date());
        ordersMapper.insert(orders);
//      3、保存订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        orderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(orderStatus);
    }
}
