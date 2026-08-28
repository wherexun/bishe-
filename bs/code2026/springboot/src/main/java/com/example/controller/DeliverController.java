package com.example.controller;

import com.example.common.Result;
import com.example.entity.Deliver;
import com.example.service.DeliverService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端操作接口
 **/
@RestController
@RequestMapping("/deliver")
public class DeliverController {

    @Resource
    private DeliverService deliverService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Deliver deliver) {
        deliverService.add(deliver);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        deliverService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Deliver deliver) {
        deliverService.updateById(deliver);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Deliver deliver = deliverService.selectById(id);
        return Result.success(deliver);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Deliver deliver) {
        List<Deliver> list = deliverService.selectAll(deliver);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Deliver deliver,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Deliver> page = deliverService.selectPage(deliver, pageNum, pageSize);
        return Result.success(page);
    }

}
