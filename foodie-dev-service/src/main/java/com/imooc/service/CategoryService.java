package com.imooc.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;

import java.util.List;

public interface CategoryService {
    /**
     * 查询所有一级分类
     * @return
     */
    List<Category> queryRootLevelCat();

    /**
     * 获取商品子分类
     * @param rootCatId
     * @return
     */
    List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 查询每个一级分类下的最新6条商品数据
     * @param rootCatId
     * @return
     */
    List<NewItemsVO> getSixNewItems(Integer rootCatId);
}
