package com.imooc.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.ItemsComments;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface ItemsCommentsMapperCustom {
    void saveComments(Map<String,Object> map);
}