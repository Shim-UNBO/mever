package com.mever.api.domain;

import com.mever.api.domain.payment.service.PaymentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String home(){
        return "index";
    }
    @Autowired
    PaymentService paymentService;

    @GetMapping("/success")
    @ApiOperation(value = "결제 성공 리다이렉트", notes = "결제 성공 시 최종 결제 승인 요청을 보냅니다.")
    public String requestFinalPayments(
            @ApiParam(value = "토스 측 결제 고유 번호", required = true) @RequestParam String paymentKey,
            @ApiParam(value = "우리 측 주문 고유 번호", required = true) @RequestParam String orderId,
            @ApiParam(value = "실제 결제 금액", required = true) @RequestParam Long amount,
            @ApiParam(value = "실제 결제 금액", required = true) @RequestParam String url,
            HttpServletRequest request
    ) throws Exception {
        try {
            Map<String, String[]> paraMap = request.getParameterMap();
            for (String name : paraMap.keySet()){
                String[] values = paraMap.get(name);
                System.out.println(name+" = "+ Arrays.toString(values));
            }
            System.out.println("paymentKey = " + paymentKey);
            System.out.println("orderId = " + orderId);
            System.out.println("amount = " + amount);
            System.out.println("url = " + url);

            String result = paymentService.requestFinalPayment(paymentKey, orderId, amount).toString();

            return "redirect:"+url;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @RequestMapping("/autoSuccess")
    @ApiOperation(value = "자동 결제 성공 리다이렉트", notes = "자동 결제 성공 시 최종 결제 승인 요청을 보냅니다.")
    public String autoFinalPayments(
            @ApiParam(value = "토스 측 자동 결제 고유 번호", required = true) @RequestParam String authKey,
            @ApiParam(value = "우리 측 주문 고객 고유 번호", required = true) @RequestParam String customerKey,
//            @ApiParam(value = "우리 측 주문 고객 고유 번호", required = true) @RequestParam String url,
            HttpServletRequest request) throws Exception {
        try {
            Map<String, String[]> paraMap = request.getParameterMap();
//            for (String name : paraMap.keySet()){
//                String[] values = paraMap.get(name);
//                System.out.println(name+" = "+ Arrays.toString(values));
//            }
            String result = paymentService.autoFinalPayment(authKey,customerKey).toString();
//            String url = request.getContextPath();
            String url = "https://mever.me/art_gallery/";

            return "redirect:"+url;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
            //sm@mever.me
            // mever12!@
        }
    }
}
