package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.enums.YseOrNo;
import com.imooc.pojo.Orders;
import com.imooc.pojo.bo.center.OrderItemsCommentBO;
import com.imooc.service.center.MyCommentsService;
import com.imooc.service.center.MyOrderService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "用户中心评论模块",tags = {"用户中心评论模块相关接口"})
@RestController
@RequestMapping("/mycomments")
public class MyCommentsController extends BaseController {

    @Autowired
    private MyOrderService myOrderService;
    @Autowired
    private MyCommentsService myCommentsService;

    @ApiOperation(value="查询评论列表", notes="查询评论列表", httpMethod = "GET")
    @PostMapping("/pending")
    public IMOOCJSONResult pending(@RequestParam String userId,@RequestParam String orderId) throws Exception {
        IMOOCJSONResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()){
            return checkResult;
        }
        Orders orders = (Orders) checkResult.getData();
        if (orders.getIsComment() == YseOrNo.YES.type){
            return IMOOCJSONResult.errorMsg("该订单已经评价");
        }
        boolean result = myOrderService.updateReceiveOrderStatus(orderId);
        if (!result){
            return IMOOCJSONResult.errorMsg("确认收货失败");
        }
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value="保存评论列表", notes="保存评论列表", httpMethod = "POST")
    @PostMapping("/saveList")
    public IMOOCJSONResult saveList(@RequestParam String userId, @RequestParam String orderId,
                                    @RequestBody List<OrderItemsCommentBO> commentList) throws Exception {
//      判断用户和订单是否关联
        IMOOCJSONResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()){
            return checkResult;
        }
//      评论内容list不能为空
        if (CollectionUtils.isEmpty(commentList)){
            return IMOOCJSONResult.errorMsg("评论内容不能为空");
        }
        myCommentsService.saveComments(orderId,userId,commentList);
        return IMOOCJSONResult.ok();
    }
    private IMOOCJSONResult checkUserOrder(String userId,String orderId){
        Orders orders = myOrderService.queryMyOrder(userId, orderId);
        if (orders == null){
            return IMOOCJSONResult.errorMsg("订单不存在");
        }
        return IMOOCJSONResult.ok(orders);
    }
}
