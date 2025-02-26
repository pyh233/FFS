package com.example.flyfishshop.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AlipayConfig implements WebMvcConfigurer {
    @Value("${alipay.app-id}")
    private String appId;

    @Value("${alipay.merchant-private-key}")
    private String merchantPrivateKey;

    @Value("${alipay.alipay-public-key}")
    private String alipayPublicKey;

    @Value("${alipay.gateway-url}")
    private String gatewayUrl;
    //创建一个AlipayClient实例
    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(gatewayUrl,
                appId, merchantPrivateKey, "json", "UTF-8",
                alipayPublicKey, "RSA2");
    }
}
