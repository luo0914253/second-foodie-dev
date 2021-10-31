package com.imooc.controller;

import com.imooc.enums.YseOrNo;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.*;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.service.ItemService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "商品接口",tags = {"商品信息展示的相关接口"})
@RestController
@RequestMapping("/items")
public class ItemsController {
    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情",notes = "查询商品详情",httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public IMOOCJSONResult info(@PathVariable String itemId){
        ItemInfoVO itemInfoVO = itemService.queryItemById(itemId);
        return IMOOCJSONResult.ok(itemInfoVO);
    }
    @ApiOperation(value = "查询商品评价等级",notes = "查询商品评价等级",httpMethod = "GET")
    @GetMapping("/commentLevel")
    public IMOOCJSONResult commentLevel(@RequestParam String itemId){
        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        CommentLevelCountsVO commentLevelCountsVO = itemService.queryCounts(itemId);
        return IMOOCJSONResult.ok(commentLevelCountsVO);
    }
    @ApiOperation(value = "查询商品评论",notes = "查询商品评论",httpMethod = "GET")
    @GetMapping("/comments")
    public IMOOCJSONResult comments(@RequestParam String itemId,
                                    @RequestParam Integer level,
                                    @RequestParam Integer page,
                                    @RequestParam Integer pageSize){
        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = 10;
        }
        PagedGridResult pagedGridResult = itemService.queryPagedComments(itemId, level, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }
    @ApiOperation(value = "搜索商品列表",notes = "搜索商品列表",httpMethod = "GET")
    @GetMapping("/search")
    public IMOOCJSONResult search(@RequestParam String keywords,
                                    @RequestParam String sort,
                                    @RequestParam Integer page,
                                    @RequestParam Integer pageSize){
        if (StringUtils.isBlank(keywords)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = 10;
        }
        PagedGridResult pagedGridResult = itemService.searchItems(keywords, sort, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }
    @ApiOperation(value = "通过分类id搜索商品列表",notes = "通过分类id搜索商品列表",httpMethod = "GET")
    @GetMapping("/catItems")
    public IMOOCJSONResult catItems(@RequestParam Integer catId,
                                    @RequestParam String sort,
                                    @RequestParam Integer page,
                                    @RequestParam Integer pageSize){
        if (catId == null) {
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = 10;
        }
        PagedGridResult pagedGridResult = itemService.searchItems(catId, sort, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }
    @ApiOperation(value = "根据商品规格ids查找最新的商品数据", notes = "根据商品规格ids查找最新的商品数据", httpMethod = "GET")
    @GetMapping("/refresh")
    public IMOOCJSONResult refresh(
            @ApiParam(name = "itemSpecIds", value = "拼接的规格ids", required = true, example = "1001,1003,1005")
            @RequestParam String itemSpecIds) {

        if (StringUtils.isBlank(itemSpecIds)) {
            return IMOOCJSONResult.ok();
        }

        List<ShopcartVO> list = itemService.queryItemBySpecIds(itemSpecIds);

        return IMOOCJSONResult.ok(list);
    }
}
