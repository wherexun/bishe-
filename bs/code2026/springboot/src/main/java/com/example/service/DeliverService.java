package com.example.service;

import com.example.entity.Deliver;
import com.example.mapper.DeliverMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务处理
 **/
@Service
public class DeliverService {

    @Resource
    private DeliverMapper deliverMapper;

    /**
     * 新增
     */
    public void add(Deliver deliver) {
        deliverMapper.insert(deliver);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        deliverMapper.deleteById(id);
    }

    /**
     * 修改
     */
    public void updateById(Deliver deliver) {
        deliverMapper.updateById(deliver);
    }

    /**
     * 根据ID查询
     */
    public Deliver selectById(Integer id) {
        return deliverMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Deliver> selectAll(Deliver deliver) {
        return deliverMapper.selectAll(deliver);
    }

    /**
     * 分页查询
     */
    public PageInfo<Deliver> selectPage(Deliver deliver, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Deliver> list = deliverMapper.selectAll(deliver);
        return PageInfo.of(list);
    }


}
