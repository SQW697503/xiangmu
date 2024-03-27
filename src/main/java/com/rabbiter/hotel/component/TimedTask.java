package com.rabbiter.hotel.component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.domain.Order;
import com.rabbiter.hotel.service.OrderService;
import com.rabbiter.hotel.service.RoomService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Component
public class TimedTask {

    @Resource
    private OrderService orderService;
    @Resource
    private RoomService roomService;

    /**
     * 定时任务方法
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void scheduled() {
        // 创建查询条件包装器
        QueryWrapper queryWrapper = new QueryWrapper();
        // 获取当前时间
        Date nowDate = new Date();
        // 设置查询条件：离店时间小于当前时间
        queryWrapper.lt("leave_time", nowDate);
        // 设置查询条件：订单状态为1（进行中）

        queryWrapper.eq("flag", 1);

        try{
            // 查询满足条件的订单列表
            List<Order> orderList = orderService.list(queryWrapper);
            if (0 != orderList.size()) {
                for (Order order : orderList) {
                    // 将订单状态设置为3（已完成）
                    order.setFlag(3);
                    // 标记房间为已完成
                    roomService.finishRoom(order.getRoomId());
                }
                // 批量更新订单列表
                orderService.updateBatchById(orderList);
            }

        }catch (Exception e){
            // 异常处理
        }

    }
}
