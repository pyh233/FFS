package com.example.flyfishshop.api;

import com.example.flyfishshop.model.Order;
import com.example.flyfishshop.model.search.SearchOrderModel;
import com.example.flyfishshop.service.OrderService;
import com.example.flyfishshop.util.validate.CommonEditGroup;
import com.example.flyfishshop.util.JsonResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/api/v1/order",produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderApi {
    OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<JsonResult> getOrder(SearchOrderModel searchOrderModel,
                                               @RequestParam(value = "page",defaultValue = "1") Integer pageNo,
                                               @RequestParam(value = "limit",defaultValue = "2") Integer pageSize) {
        Page<?> page = new Page<>(pageNo, pageSize);
        List<Order> orderList = orderService.getAllOrders(searchOrderModel, page);
        PageInfo<?> pi = new PageInfo<>(orderList);
        JsonResult jr = null;
        if(!orderList.isEmpty()){
            jr = JsonResult.success("查询成功", orderList);
            jr.setCount((int)pi.getTotal());
            return ResponseEntity.ok(jr);
        }else {
            return ResponseEntity.ok(JsonResult.fail("查询失败"));
        }
    }
    @PatchMapping
    public ResponseEntity<JsonResult> updateOrder(@Validated(CommonEditGroup.class) Order order) {
        boolean success = orderService.patchOrder(order);
        if(success){
            return ResponseEntity.ok(JsonResult.success("修改信息成功",null));
        }else {
            return ResponseEntity.ok(JsonResult.fail("修改信息失败"));
        }
    }

}
