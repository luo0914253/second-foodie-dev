package com.imooc.controller;

import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemInfoVO;
import com.imooc.pojo.vo.ShopcartVO;
import com.imooc.service.ItemService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "购物车接口controller",tags = {"购物车接口相关的api"})
@RestController
@RequestMapping("/shopcart")
public class ShopCatController {

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "添加商品到购车",notes = "添加商品到购车",httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@RequestParam String userId,
                                @RequestBody ShopcartBO shopcartBO,
                                HttpServletRequest request,
                                HttpServletResponse response){
        if (StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("");
        }
//      TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "从购物车中删除商品",notes = "从购物车中删除商品",httpMethod = "POST")
    @PostMapping("/del")
    public IMOOCJSONResult del(@RequestParam String userId,
                                @RequestParam String itemSpecId,
                                HttpServletRequest request,
                                HttpServletResponse response){
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)){
            return IMOOCJSONResult.errorMsg("");
        }
//      TODO 用户在页面删除购物车中的商品数据，如果此时用户已登录，则需要同步删除后端购物车中的商品
        return IMOOCJSONResult.ok();
    }


}
