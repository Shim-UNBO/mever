package com.mever.api.domain;

import com.mever.api.domain.payment.service.PaymentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            HttpServletRequest request
    ) throws Exception {
        try {
            System.out.println("paymentKey = " + paymentKey);
            System.out.println("orderId = " + orderId);
            System.out.println("amount = " + amount);

            String result = paymentService.requestFinalPayment(paymentKey, orderId, amount).toString();

            return "redirect:https://mever.me";
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
}
