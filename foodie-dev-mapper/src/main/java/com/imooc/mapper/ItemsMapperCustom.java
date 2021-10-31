package com.imooc.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsComments;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.SearchItemsVO;
import com.imooc.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ItemsMapperCustom {

    List<ItemCommentVO> queryItemComments(@Param("map") Map<String,Object> map);

    List<SearchItemsVO> searchItems(@Param("map") Map<String,Object> map);

    List<SearchItemsVO> searchItemsByCatId(@Param("map") Map<String,Object> map);

    public List<ShopcartVO> queryItemsBySpecIds(@Param("paramsList") List specIdsList);
}