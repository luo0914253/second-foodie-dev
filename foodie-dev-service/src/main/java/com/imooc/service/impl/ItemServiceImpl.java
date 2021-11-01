package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.enums.CommentLevel;
import com.imooc.enums.YseOrNo;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.*;
import com.imooc.service.ItemService;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private ItemsImgMapper itemsImgMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired
    private ItemsParamMapper itemsParamMapper;
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;
    @Autowired
    private ItemsMapperCustom itemsMapperCustom;
    /**
     * 查询商品详情
     * @param itemId
     * @return
     */
    @Override
    public ItemInfoVO queryItemById(String itemId) {
        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(queryItemsById(itemId));
        itemInfoVO.setItemImgList(queryItemsImageById(itemId));
        itemInfoVO.setItemSpecList(queryItemsSpecById(itemId));
        itemInfoVO.setItemParams(queryItemsParamById(itemId));
        return itemInfoVO;
    }

    /**
     * 查询商品详情
     * @param itemId
     * @return
     */
    @Override
    public Items queryItem(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    /**
     * 查询商品评价等级
     * @return
     */
    @Override
    public CommentLevelCountsVO queryCounts(String itemId) {
        Integer totalCounts = getCommentCounts(itemId, null);
        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.type);
        return new CommentLevelCountsVO(totalCounts,goodCounts,normalCounts,badCounts);
    }

    /**
     * 根据商品id查询商品的评价
     * @param itemId
     * @param level
     * @return
     */
    @Override
    public PagedGridResult queryPagedComments(String itemId, Integer level,Integer page,Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("itemId",itemId);
        map.put("level",level);
        PageHelper.startPage(page,pageSize);
        List<ItemCommentVO> itemCommentVOS = itemsMapperCustom.queryItemComments(map);
        itemCommentVOS.forEach(itemCommentVO -> itemCommentVO.setNickName(DesensitizationUtil.commonDisplay(itemCommentVO.getNickName())));
        return setterPagedGrid(itemCommentVOS,page);
    }

    /**
     * 搜索商品列表
     *
     * @param keyword
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PagedGridResult searchItems(String keyword, String sort, Integer page, Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("keyword",keyword);
        map.put("sort",sort);
        PageHelper.startPage(page,pageSize);
        List<SearchItemsVO> searchItemsVOS = itemsMapperCustom.searchItems(map);
        return setterPagedGrid(searchItemsVOS,page);
    }

    /**
     * 通过分类id搜索商品列表
     *
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("catId",catId);
        map.put("sort",sort);
        PageHelper.startPage(page,pageSize);
        List<SearchItemsVO> searchItemsVOS = itemsMapperCustom.searchItemsByCatId(map);
        return setterPagedGrid(searchItemsVOS,page);
    }

    /**
     * 根据商品规格ids查找最新的商品数据
     * @param itemSpecIds
     * @return
     */
    @Override
    public List<ShopcartVO> queryItemBySpecIds(String itemSpecIds) {
        String[] ids = itemSpecIds.split(",");
        List<String> specIdsList = new ArrayList<>();
        Collections.addAll(specIdsList,ids);
        List<ShopcartVO> shopcartVOS = itemsMapperCustom.queryItemsBySpecIds(specIdsList);
        return shopcartVOS;
    }

    /**
     * 根据商品规格id查找商品
     * @param itemSpecId
     * @return
     */
    @Override
    public ItemsSpec queryItemSpecById(String itemSpecId) {
        return itemsSpecMapper.selectByPrimaryKey(itemSpecId);
    }

    /**
     * 根据商品id查找主图URL
     *
     * @param itemId
     * @return
     */
    @Override
    public String queryItemMainImgById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setIsMain(YseOrNo.YES.type);
        itemsImg.setItemId(itemId);
        return itemsImgMapper.selectOne(itemsImg).getUrl();
    }

    /**
     * 扣减库存
     *
     * @param specId
     * @param buyCounts
     */
    @Override
    public void decreaseItemSpecStock(String specId, int buyCounts) {
        Map<String,Object> map = new HashMap<>();
        map.put("specId",specId);
        map.put("buyCounts",buyCounts);
        int result = itemsSpecMapper.decreaseItemSpecStock(map);
        if (result != 1){
            throw new RuntimeException("创建订单失败，原因：库存不足");
        }
    }

    private PagedGridResult setterPagedGrid(List<?> list,Integer page){
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setPage(page);
        pagedGridResult.setRows(list);
        pagedGridResult.setTotal(pageList.getPages());
        pagedGridResult.setRecords(pageList.getTotal());
        return pagedGridResult;
    }
    private Integer getCommentCounts(String itemId,Integer level){
        ItemsComments condition = new ItemsComments();
        condition.setItemId(itemId);
        if (level != null){
            condition.setCommentLevel(level);
        }
        int count = itemsCommentsMapper.selectCount(condition);
        return count;
    }

    private Items queryItemsById(String itemId){
        return itemsMapper.selectByPrimaryKey(itemId);
    }
    private List<ItemsImg> queryItemsImageById(String itemId){
        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        List<ItemsImg> itemsImgs = itemsImgMapper.selectByExample(example);
        return itemsImgs;
    }
    private List<ItemsSpec> queryItemsSpecById(String itemId){
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        List<ItemsSpec> itemsSpecs = itemsSpecMapper.selectByExample(example);
        return itemsSpecs;
    }
    private ItemsParam queryItemsParamById(String itemId){
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        ItemsParam itemsParam = itemsParamMapper.selectOneByExample(example);
        return itemsParam;
    }
}
