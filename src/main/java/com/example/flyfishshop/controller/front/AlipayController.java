package com.example.flyfishshop.controller.front;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.example.flyfishshop.model.Order;
import com.example.flyfishshop.model.OrderState;
import com.example.flyfishshop.model.User;
import com.example.flyfishshop.service.OrderService;
import com.example.flyfishshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class AlipayController {
    // @Bean配置了alipayClient
    private AlipayClient alipayClient;
    private OrderService orderService;
    private UserService userService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAlipayClient(AlipayClient alipayClient) {
        this.alipayClient = alipayClient;
    }

    @Value("${alipay.notify-url}")
    private String notifyUrl;

    @Value("${alipay.return-url}")
    private String returnUrl;

    @Value("${alipay.alipay-public-key}")
    private String alipayPublicKey;

    //向支付宝发送订单请求
    @GetMapping(value = "/order/alipay", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String alipay(Integer id) throws AlipayApiException {
        Order order = orderService.getOrderById(id);
        User user = userService.findById(order.getMemberId());

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(returnUrl);
        request.setNotifyUrl(notifyUrl);

        JSONObject bizContent = new JSONObject();
        //外部的订单号
        bizContent.put("out_trade_no", order.getOrderNo());
        bizContent.put("total_amount", order.getTotalPay());
        bizContent.put("subject", order.getOrderNo() + " " + user.getAccount());
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());

        AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
        if (response.isSuccess()) {
            return response.getBody();//是一个html片段
        } else {
            throw new RuntimeException("创建订单失败");
        }
    }

    // 支付宝付款成功会向此处发送异步请求
    @PostMapping(value = "/alipay/notify", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String alipayNotify(@RequestParam Map<String, String> params) throws AlipayApiException {
        //校验
        boolean signVerified = AlipaySignature.rsaCheckV1(params,
                alipayPublicKey, "UTF-8", "RSA2");
        if (signVerified) {
            String orderNo = params.get("out_trade_no");//获取订单号
            Order order = orderService.getOrderByOrderNo(orderNo);//获取订单
            // TODO:?
            Order od = new Order();
            od.setId(order.getId());

            od.setState(OrderState.PAYED);//修改为已支付
            od.setPayTime(LocalDateTime.now());

            orderService.patchPay(od);//只修改订单状态

            //System.out.println(JSONObject.toJSONString(params));

            return "success";
        } else {
            return "fail";
        }
    }
}
