package com.imooc.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.ItemsSpec;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface ItemsSpecMapper extends MyMapper<ItemsSpec> {
    int decreaseItemSpecStock(Map<String,Object> map);
}