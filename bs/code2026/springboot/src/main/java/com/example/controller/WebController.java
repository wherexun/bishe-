package com.example.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.common.Result;
import com.example.entity.*;
import com.example.mapper.OrderDetailMapper;
import com.example.service.*;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;


@RestController
public class WebController {

    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;
    @Resource
    OrdersService ordersService;
    @Resource
    GoodsService goodsService;
    @Resource
    CategoryService categoryService;
    @Resource
    OrderDetailMapper orderDetailMapper;


    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account ac = null;
        if ("管理员".equals(account.getRole())) {
            ac = adminService.login(account);
        }
        if ("普通用户".equals(account.getRole())) {
            ac = userService.login(account);
        }
        if (ac == null) {
            return Result.error("登录失败，用户不存在");
        }
        return Result.success(ac);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (!user.getPassword().equals(user.getNewPassword())) {
            return Result.error("两次输入密码不一致");
        }
        userService.add(user);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if ("管理员".equals(account.getRole())) {
            adminService.updatePassword(account);
        }
        if ("普通用户".equals(account.getRole())) {
            userService.updatePassword(account);
        }
        return Result.success();
    }

    // 数据统计使用的接口
    @GetMapping("/count")
    public Result count() {
        List<Orders> ordersList = ordersService.selectAll(null).stream().filter(orders -> !orders.getStatus().equals("已取消")).toList();
        BigDecimal total = ordersList.stream().map(Orders::getTotal).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        String todayDate = DateUtil.today();  // 2025-12-05
        BigDecimal today = ordersList.stream().filter(orders -> orders.getTime().contains(todayDate))
                .map(Orders::getTotal).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        Integer goods = goodsService.selectAll(null).size();
        Integer user = userService.selectByAll(null).size();
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("today", today);
        map.put("goods", goods);
        map.put("user", user);
        return Result.success(map);
    }

    @GetMapping("/selectLine")
    public Result selectLine() {
        Date date = new Date();
        DateTime start = DateUtil.offsetDay(date, -6);
        List<DateTime> dateTimes = DateUtil.rangeToList(start, date, DateField.DAY_OF_YEAR);
        List<String> dateStrList = dateTimes.stream().map(dateTime -> DateUtil.format(dateTime, "MM-dd")).sorted().toList();
        List<Orders> ordersList = ordersService.selectAll(null).stream().filter(orders -> !orders.getStatus().equals("已取消")).toList();
        int year = DateUtil.year(date);
        ArrayList<BigDecimal> countList = new ArrayList<>();
        for (String day : dateStrList) {
            // 包含  年月日的  订单
            BigDecimal total = ordersList.stream().filter(o -> o.getTime().contains(String.valueOf(year)) && o.getTime().contains(day))
                    .map(Orders::getTotal).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            countList.add(total);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("date", dateStrList);
        map.put("count", countList);
        return Result.success(map);
    }

    @GetMapping("/selectPie")
    public Result selectPie() {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Category> categoryList = categoryService.selectAll(null);
        List<OrderDetail> orderDetailList = orderDetailMapper.selectAll(null);
        for (Category category : categoryList) {
            BigDecimal total = BigDecimal.ZERO;
            for (OrderDetail orderDetail : orderDetailList) {
                // 排除已取消的订单
                Orders orders = ordersService.selectById(orderDetail.getOrderId());
                if (orders == null || "已取消".equals(orders.getStatus())) {
                    continue;
                }
                Goods goods = goodsService.selectById(orderDetail.getGoodsId());
                if (goods == null || goods.getCategoryId() == null) {
                    continue;
                }
                if (goods.getCategoryId().equals(category.getId())) {
                    // 按这条明细的商品小计累加，而不是整个订单总价
                    total = total.add(orderDetail.getGoodsPrice().multiply(BigDecimal.valueOf(orderDetail.getNum())));
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("name", category.getName());
            map.put("value", total);
            if (total.compareTo(BigDecimal.ZERO) > 0) {
                list.add(map);
            }
        }
        return Result.success(list);
    }

}
