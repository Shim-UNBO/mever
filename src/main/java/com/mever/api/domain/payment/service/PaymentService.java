package com.mever.api.domain.payment.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mever.api.domain.member.entity.Member;
import com.mever.api.domain.member.repository.MemberRepository;
import com.mever.api.domain.payment.dto.CancelOrderDto;
import com.mever.api.domain.payment.dto.PaymentResHandleDto;
import com.mever.api.domain.payment.entity.Orders;
import com.mever.api.domain.payment.repository.CancelOrderRepository;
import com.mever.api.domain.payment.repository.OrderMapper;
import com.mever.api.domain.payment.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final CancelOrderRepository cancelOrderRepository;
    private final MemberRepository memberRepository;
    @Value("${payments.toss.test_client_api_key}")
    private String testClientApiKey;

    @Value("${payments.toss.test_secret_api_key}")
    private String testSecretApiKey;

    private String successCallBackUrl = "/success";

    private String failCallBackUrl = "/fail";

    private String tossOriginUrl = "https://api.tosspayments.com/v1/payments/";

    @Transactional
    public PaymentResHandleDto requestPayments(PaymentResHandleDto paymentResHandleDto) throws Exception {


        /*if (amount == null || amount != 3000) {
            throw new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_PRICE);
        }

        if (!payType.equals("CARD") && !payType.equals("카드")) {
            throw new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_PAY_TYPE);
        }

        if (!orderName.equals(OrderNameType.상품명1.name()) &&
                !orderName.equals(OrderNameType.상품명2.name())) {
            throw new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_NAME);
        }*/
        try {
            Member member = memberRepository.findByEmail(paymentResHandleDto.getEmail()).orElse(null);
            member.setName(paymentResHandleDto.getName());
            memberRepository.save(member);

            Orders orders = paymentResHandleDto.toOrderBuilder();
            orderRepository.save(orders);
            paymentResHandleDto.setSuccessUrl(successCallBackUrl);
            paymentResHandleDto.setFailUrl(failCallBackUrl);
            paymentResHandleDto.setOrderId(orders.getOrderId());
            return paymentResHandleDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public PaymentResHandleDto requestFinalPayment(String paymentKey, String orderId, Long amount) throws Exception {
        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        testSecretApiKey = testSecretApiKey + ":";
        String encodedAuth = new String(Base64.getEncoder().encode(testSecretApiKey.getBytes(StandardCharsets.UTF_8)));
        headers.setBasicAuth(encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        JSONObject param = new JSONObject();
        param.put("orderId", orderId);
        param.put("amount", amount);
        JSONObject resJson =  rest.postForEntity(
                tossOriginUrl + paymentKey,
                new HttpEntity<>(param, headers),
                JSONObject.class
        ).getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String jsonString = objectMapper.writeValueAsString(resJson);
        PaymentResHandleDto paymentResHandleDtorder = objectMapper.readValue(jsonString,PaymentResHandleDto.class);
        paymentResHandleDtorder.setMId(resJson.get("mId").toString());

        System.out.println(resJson);
        if(resJson.get("card")!=null){
            JSONParser parser = new JSONParser();
            String card = objectMapper.writeValueAsString(resJson.get("card"));
            Object obj = parser.parse( card );
            JSONObject jsonObj = (JSONObject) obj;
            paymentResHandleDtorder.setNumber(jsonObj.get("number").toString());
            paymentResHandleDtorder.setCompany(jsonObj.get("company").toString());
        }

        if(resJson.get("easyPay")!=null){
            JSONParser parser = new JSONParser();
            String easyPay= objectMapper.writeValueAsString(resJson.get("easyPay"));
            Object obj = parser.parse(easyPay);
            JSONObject jsontest = (JSONObject) obj;
            paymentResHandleDtorder.setCompany(jsontest.get("provider").toString());
        }
        Orders orders = orderRepository.findByOrderId(orderId);
        orderRepository.save(paymentResHandleDtorder.toOrderEntity(orders));
        return paymentResHandleDtorder;
    }

    @Transactional
    public List<PaymentResHandleDto> paymentList() {
        List<PaymentResHandleDto> paymentRes=orderMapper.getPayList();
        return paymentRes;
    }

    @Transactional
    public  ResponseEntity cancelOrder(String paymentKey, String cancelReason) throws Exception {

        RestTemplate rest = new RestTemplate();

        URI uri = URI.create(tossOriginUrl + paymentKey + "/cancel");

        HttpHeaders headers = new HttpHeaders();
        byte[] secretKeyByte = (testSecretApiKey + ":").getBytes(StandardCharsets.UTF_8);
        headers.setBasicAuth(new String(Base64.getEncoder().encode(secretKeyByte)));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        JSONObject param = new JSONObject();
        param.put("cancelReason", cancelReason);

        PaymentResHandleDto paymentResHandleDto;
        try {
            paymentResHandleDto = rest.postForObject(
                    uri,
                    new HttpEntity<>(param, headers),
                    PaymentResHandleDto.class
            );
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        if (paymentResHandleDto == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        CancelOrderDto cancelOrderDto;
        System.out.println(paymentResHandleDto);
        Orders orders = orderRepository.findByPaymentKey(paymentKey);
        cancelOrderRepository.save(paymentResHandleDto.toCancelOrder(orders));
        orders.setStatus(paymentResHandleDto.getStatus());
        orderRepository.save(orders);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
