package com.mever.api.domain.email.service;

import com.mever.api.domain.email.dto.EmailDto;
import com.mever.api.domain.email.dto.ReservationEmailDto;
import com.mever.api.domain.email.dto.SmsDto;
import com.mever.api.domain.email.entity.ReservationMail;
import com.mever.api.domain.email.entity.SendHistory;
import com.mever.api.domain.email.repository.ReservationMailRepository;
import com.mever.api.domain.email.repository.SendRepository;
import com.mever.api.domain.member.entity.Member;
import com.mever.api.domain.member.repository.MemberRepository;
import com.mever.api.domain.payment.dto.CancelOrderDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendService {
    @Value("${spring.mail.username}")
    private String FROM_ADDRESS;
    private final JavaMailSender emailSender;
    @Value("${send.sms.userid}")
    private String smsUserid;
    @Value("${send.sms.userkey}")
    private String smsUserkey;
    @Autowired
    private MemberRepository memberRepository;
    private final SendRepository sendRepository;
    private final ReservationMailRepository reservationMailRepository;

    @Transactional
    public ResponseEntity sendMultipleMessage(EmailDto mailDto, MultipartFile file) throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //메일 제목 설정
        helper.setSubject(mailDto.getTitle());

        //참조자 설정
        //helper.setCc(mailDto.getCcAddress());

        helper.setText(mailDto.getContent(), false);

        helper.setFrom(FROM_ADDRESS);

        //첨부 파일 설정
       /* String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        helper.addAttachment(MimeUtility.encodeText(fileName, "UTF-8", "B"), new ByteArrayResource(IOUtils.toByteArray(file.getInputStream())));
*/
      //  수신자 개별 전송
        /*for(String s : mailDto.getAddress()) {
        	helper.setTo(s);
        	emailSender.send(message);
        }*/
        //수신자 한번에 전송
        helper.setTo(mailDto.getAddress());
        emailSender.send(message);
        log.info("mail multiple send complete.");
        EmailDto emailDto= EmailDto.builder()
                .content(mailDto.getContent())
                .address(mailDto.getAddress())
                .phone(mailDto.getPhone())
                .title(mailDto.getPhone())
                .build();
        sendRepository.save(emailDto.toMailBuilder());

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity saveReservationMail(List<ReservationEmailDto> list) throws MessagingException, IOException {
        LocalDate today = LocalDate.now();
        LocalDate sendDate = today;
       //TODO 스케쥴 돌릴 때 리스트로 받아와서 작업해야함

        LocalDate formattedDate = LocalDate.parse(sendDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        for(int i =0; i<list.size();i++){
            switch (list.get(i).getPeriod()) {
                case "new":
                    sendDate = today.plusDays(1);
                    break;
                case "day":
                    sendDate = today.plusDays(1);
                    break;
                case "week":
                    sendDate = today.plusWeeks(1);
                    break;
                case "month":
                    sendDate = today.plusMonths(1);
                    break;
                case "year":
                    sendDate = today.plusYears(1);
                    break;
                default:
                    // 유효하지 않은 period 값이 들어온 경우
                    break;
//        )
            }
            ReservationEmailDto reservationEmailDto=ReservationEmailDto.builder()
                    .email(list.get(i).getEmail())
                    .title(String.valueOf(list.get(i).getTitle()))
                    .content(String.valueOf(list.get(i).getContent()))
                    .sendDate(sendDate.toString())
                    .period(list.get(i).getPeriod())
                    .build();
            reservationMailRepository.save(reservationEmailDto.toReservationMailBuilder());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Transactional
    public void sendMessage(EmailDto mailDto) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //메일 제목 설정
        helper.setSubject(mailDto.getTitle());

        //참조자 설정
        //helper.setCc(mailDto.getCcAddress());

        helper.setText(mailDto.getContent(), false);

        helper.setFrom(FROM_ADDRESS);

        //첨부 파일 설정
       /* String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        helper.addAttachment(MimeUtility.encodeText(fileName, "UTF-8", "B"), new ByteArrayResource(IOUtils.toByteArray(file.getInputStream())));
*/
        //  수신자 개별 전송
        /*for(String s : mailDto.getAddress()) {
        	helper.setTo(s);
        	emailSender.send(message);
        }*/
        //수신자 한번에 전송
        helper.setTo(mailDto.getAddress());
        emailSender.send(message);
        SmsDto smsDto= SmsDto.builder()
                .msg(mailDto.getContent())
                .email(mailDto.getAddress())
                .phone(mailDto.getPhone())
                .type("mail")
                .build();
        sendRepository.save(smsDto.toSendBuilder());

    }
    @Transactional
    public void schedulEmail(ReservationEmailDto reservationEmailDto) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //메일 제목 설정
        helper.setSubject(reservationEmailDto.getTitle());
        helper.setText(reservationEmailDto.getContent(), false);
        helper.setFrom(FROM_ADDRESS);
        helper.setTo(reservationEmailDto.getEmail());
        emailSender.send(message);

        SmsDto smsDto= SmsDto.builder()
                .msg(reservationEmailDto.getContent())
                .email(reservationEmailDto.getEmail())
                .phone(reservationEmailDto.getPhone())
                .type("mail")
                .build();
        sendRepository.save(smsDto.toSendBuilder());

        LocalDate today = LocalDate.now();
        LocalDate sendDate = today;

        LocalDate formattedDate = LocalDate.parse(sendDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        String content="";

            switch (reservationEmailDto.getPeriod()) {
                case "new":
                    sendDate = today.plusDays(1);
                    content= String.valueOf(reservationEmailDto.getContent());
                    Member member = memberRepository.findByEmail(reservationEmailDto.getEmail()).orElse(null);
                    member.setAfterDay(Long.valueOf(member.getAfterDay())+1);
                    memberRepository.save(member);
                    break;
                case "day":
                    sendDate = today.plusDays(1);
                    content= String.valueOf(reservationEmailDto.getContent());
                    break;
                case "week":
                    sendDate = today.plusWeeks(1);
                    content= String.valueOf(reservationEmailDto.getContent());
                    break;
                case "month":
                    sendDate = today.plusMonths(1);
                    content= String.valueOf(reservationEmailDto.getContent());
                    break;
                case "year":
                    sendDate = today.plusYears(1);
                    content= String.valueOf(reservationEmailDto.getContent());
                    break;
                default:
                    // 유효하지 않은 period 값이 들어온 경우
                    break;
//        )
            }
            ReservationEmailDto reservationEmail=ReservationEmailDto.builder()
                    .email(reservationEmailDto.getEmail())
                    .title(reservationEmailDto.getTitle())
                    .content(content)
                    .sendDate(sendDate.toString())
                    .period(reservationEmailDto.getPeriod())
                    .build();
            reservationMailRepository.save(reservationEmail.toReservationMailBuilder());

    }
    @Transactional
    public ResponseEntity<Object> sendSms(String phone,String email) throws Exception {
        RestTemplate rest = new RestTemplate();
        rest.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        //URI uri = URI.create("http://link.smsceo.co.kr/sendsms_test.php");
        URI uri = URI.create("http://link.smsceo.co.kr/sendsms_utf8.php");

        HttpHeaders headers = new HttpHeaders();
      /*  headers.setContentType(MediaType.TEXT_HTML);*/
        //headers.add("Content-Type", "text/html; charset=UTF-8");
        //headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        //headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED,);
       // headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
        headers.setContentType(new MediaType("application", "x-www-form-urlencoded", Charset.forName("UTF-8")));

        /*JSONObject param = new JSONObject();*/
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
       /* param.put("phone", "01063645022");
        param.put("msg", "test");
        param.put("callback", "01072818209");
        param.put("userid", "mever");
        param.put("userkey", "BzxQZV1sDzVSYAdmUX4HOFZmB3QAM1MuA34=");*/
        param.add("phone", phone);
        param.add("msg", "300,000원 진행 <br>" +
                "7일간 18,550,000 마감 <br>" +
                "\"당일수익\"환급원칙<br>"+
                "https://mever.me/");
        param.add("callback", "01072818209");
        param.add("userid", smsUserid);
        param.add("return_url", "http://localhost:8080/send/sms/success");
        param.add("userkey", smsUserkey);

       SmsDto smsDto= SmsDto.builder()
               .msg(param.get("msg").toString())
               .email(email)
               .phone(param.get("phone").toString())
               .type("sms")
               .build();

        try {
          String respon=rest.postForObject(uri,  new HttpEntity<>(param , headers), String.class);
            System.out.println(respon);
            String[] params = respon.split("&");
            Map<String, String> map = new HashMap<String, String>();
           /*for (int i =0;i<1;i++)
            {
                String name = params[0].split("=")[0];
                System.out.println(name);
                String value="";
                if(para.split("=")[1]!=null||!para.split("=")[1].equals("")){
                    value = para.split("=")[1];
                }
                map.put(name, value);
            }*/
            sendRepository.save(smsDto.toSendBuilder());
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
       // if (SmsDto == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

      /*  Orders orders = orderRepository.findByPaymentKey(paymentKey);
        cancelOrderRepository.save(paymentResHandleDto.toCancelOrder(orders));
        orders.setStatus(paymentResHandleDto.getStatus());
        orderRepository.save(orders);*/
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> successSms(SmsDto smsDto) throws Exception {

        try {
            sendRepository.save(smsDto.toSendBuilder());
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> sendList(String type, String email,String phone) throws Exception {

        try {
            if((email==null||email.equals(""))&&(phone==null||phone.equals(""))){
                sendRepository.findAllByType(type);
            }
            if(email!=null||!email.equals("")){
                sendRepository.findAllByTypeAndEmail(type,email);
            }
            if(phone!=null||!phone.equals("")){
                sendRepository.findAllByTypeAndPhone(type,phone);
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
