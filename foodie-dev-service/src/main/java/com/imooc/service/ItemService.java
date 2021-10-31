package com.imooc.service;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.*;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface ItemService {
    /**
     * 查询商品详情
     * @param itemId
     * @return
     */
    ItemInfoVO queryItemById(String itemId);
    /**
     * 查询商品详情
     * @param itemId
     * @return
     */
    Items queryItem(String itemId);

    /**
     * 查询商品评价等级
     * @return
     */
    CommentLevelCountsVO queryCounts(String itemId);

    /**
     * 根据商品id查询商品的评价
     * @param itemId
     * @param level
     * @return
     */
    PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);

    /**
     * 搜索商品列表
     * @param keyword
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItems(String keyword, String sort, Integer page, Integer pageSize);
    /**
     * 通过分类id搜索商品列表
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize);

    /**
     * 根据商品规格ids查找最新的商品数据
     * @param itemSpecIds
     * @return
     */
    List<ShopcartVO> queryItemBySpecIds(String itemSpecIds);

    /**
     * 根据商品规格id查找商品
     * @param itemSpecId
     * @return
     */
    ItemsSpec queryItemSpecById(String itemSpecId);

    /**
     * 根据商品id查找主图URL
     * @param itemId
     * @return
     */
    String queryItemMainImgById(String itemId);
}
