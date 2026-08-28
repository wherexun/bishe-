package com.example.mapper;

import com.example.entity.Deliver;

import java.util.List;

/**
 * 操作deliver相关数据接口
*/
public interface DeliverMapper {

    /**
      * 新增
    */
    int insert(Deliver deliver);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Deliver deliver);

    /**
      * 根据ID查询
    */
    Deliver selectById(Integer id);

    /**
      * 查询所有
    */
    List<Deliver> selectAll(Deliver deliver);
}
