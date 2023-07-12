package com.mever.api.domain.mainAdmin.service;

import com.mever.api.domain.email.dto.EmailDto;
import com.mever.api.domain.email.service.NaverSmsSender;
import com.mever.api.domain.email.service.SendService;
import com.mever.api.domain.mainAdmin.dto.MainDto;
import com.mever.api.domain.mainAdmin.entity.ItemContents;
import com.mever.api.domain.mainAdmin.entity.MainTitle;
import com.mever.api.domain.mainAdmin.entity.Reservation;
import com.mever.api.domain.mainAdmin.repository.ItemContentsRepository;
import com.mever.api.domain.mainAdmin.repository.MainMapper;
import com.mever.api.domain.mainAdmin.repository.MainRepository;
import com.mever.api.domain.mainAdmin.repository.ReservationRepository;
import com.mever.api.domain.member.entity.Member;
import com.mever.api.domain.member.repository.MemberRepository;
import com.sun.tools.javac.Main;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainAdminService {
    private final MainRepository mainTitleRepository;
    private final ItemContentsRepository itemContentsRepository;
    private final MainMapper mainMapper;
    private final ReservationRepository reservationRepository;
    private final MainDto mainDto;
    private final SendService sendService;
    private final MemberRepository memberRepository;
    private final NaverSmsSender naverSmsSender;

    @Transactional
    public MainDto getMainTitle(Map<String,String> requestData){
        String category = requestData.get("category");
        MainDto mainDto = new MainDto();
        String title="";
        String subTitle="";
        List<MainTitle> mainTitleList;
        mainTitleList = mainTitleRepository.findByCategoryOrderBySeqDesc(category);
        if(mainTitleList.size()==0){
            return null;
        }else {
            MainTitle mainTitle = mainTitleList.get(0);
            title = mainTitle.getTitle();
            subTitle = mainTitle.getSubTitle();
        }
        mainDto.setTitle(title);
        mainDto.setSubTitle(subTitle);
        return mainDto;
    }
    @Transactional
    public List<MainTitle> getMainTitleList(Map<String,String> requestData){
        String category = requestData.get("category");
        List<MainTitle> mainTitleList;
        mainTitleList = mainTitleRepository.findByCategoryOrderBySeqDesc(category);
        if(mainTitleList.size()==0){
            return null;
        }else {
            return mainTitleList;
        }
    }
    public void updateTitle(Map<String,String> requestData){
        String category = requestData.get("category");
//            Date now = new Date();
//            String nowTime = now.toString();
            LocalDateTime now = LocalDateTime.now();
            mainDto.setCategory(requestData.get("category"));
            mainDto.setTitle(requestData.get("mainTitle"));
            mainDto.setSubTitle(requestData.get("subTitle"));
            mainDto.setInsertDate(String.valueOf(now));
            mainTitleRepository.save(mainDto.toMainTitleBuilder());
    }
    public void deleteTitle(Map<String,String> requestData){
        int seq = Integer.parseInt(requestData.get("seq"));
        MainTitle mainTitle = mainTitleRepository.getOne(seq);
        mainTitleRepository.delete(mainTitle);
    }
    public void updateContents(Map<String,String> requestData){
//        String orederName = requestData.get("orederName");
        String orderId = requestData.get("orderId");
        ItemContents itemContents = itemContentsRepository.findByOrderId(orderId);
        if (itemContents == null){
            System.out.println("orderIdNull : "+ orderId);
            mainDto.setCategory(requestData.get("category"));
            mainDto.setItemTitle(requestData.get("itemTitle"));
            mainDto.setOrderName(requestData.get("orderName"));
            mainDto.setContents(requestData.get("contents"));
            mainDto.setPhotoUrl(requestData.get("photoUrl"));
            mainDto.setVideoUrl(requestData.get("videoUrl"));
            mainDto.setImgUrl(requestData.get("imgUrl"));
            mainDto.setPrice(requestData.get("price"));
            itemContentsRepository.save(mainDto.toItemBuilder());
        }else {
            System.out.println("notNull : "+ orderId);
            itemContents.setItemTitle(requestData.get("itemTitle"));
            itemContents.setPhotoUrl(requestData.get("photoUrl"));
            itemContents.setVideoUrl(requestData.get("videoUrl"));
            itemContents.setContents(requestData.get("contents"));
            itemContents.setOrderName(requestData.get("orderName"));
            itemContents.setImgUrl(requestData.get("imgUrl"));
            itemContents.setPrice(requestData.get("price"));
            itemContentsRepository.save(itemContents);
        }
    }
    public MainDto getItemContents(String category,String orderId){
        MainDto mainDto = new MainDto();
        ItemContents itemContents = itemContentsRepository.findByOrderId(orderId);
        if(itemContents == null){
            return null;
        }else {
            mainDto.setContents(itemContents.getContents());
            mainDto.setOrderName(itemContents.getOrderName());
            mainDto.setVideoUrl(itemContents.getVideoUrl());
            mainDto.setPhotoUrl(itemContents.getPhotoUrl());
            mainDto.setPrice(itemContents.getPrice());
            mainDto.setOrderId(itemContents.getOrderId());
            mainDto.setItemTitle(itemContents.getItemTitle());
        }
        return mainDto;
    }
    @Transactional
    public List<ItemContents> getItemList(String category) {
        System.out.println(category);
        if(category==null||category.equals("")) {
            return itemContentsRepository.findAll();
        }
        return itemContentsRepository.findByCategory(category).orElse(null);
    }
    @Transactional
    public void setReservation(Map<String,String> requestData) throws Exception{
        String orderId = requestData.get("orderId");
        String reserDate = requestData.get("reservationDate");
        LocalDateTime now = LocalDateTime.now();
        //List<Reservation> reservations = reservationRepository.findByOrderId(orderId);
        List<Reservation> reservations = reservationRepository.findAll();
        //예약이 된 item이 존재할때
        if (!reservations.isEmpty()) {
            //예약 상태 변경
            if (requestData.get("status") != null && requestData.get("status") != "") {
                String seq =requestData.get("seq");
                Reservation reservation = reservationRepository.findBySeq(seq);
                reservation.setStatus(requestData.get("status"));
                reservationRepository.save(reservation);
                //이메일 보내기
                requestData.put("title",reservation.getEmail()+"님 ["+reservation.getOrderId()+"] 예약이 ["+requestData.get("status")+"] 되었습니다.");
                requestData.put("content","["+reservation.getOrderId()+"] "+reservation.getReservationDate()+" 에 예약이 ["+requestData.get("status")+"] 되었습니다.");
                requestData.put("email",reservation.getEmail());
                mailSetup(requestData);
                return;
            }
            //item 예약 중복 검사 후 예약넣기
            int existingReservationCount = 0;
            for (Reservation reservation : reservations) {
                if (reservation.getReservationDate().equals(reserDate)) {
                    existingReservationCount++;
                }
            }
            if (existingReservationCount >= 3) {
                throw new Exception("이미 세 개의 예약이 존재합니다.");
            }
            Reservation newReservation = new Reservation();
            newReservation.setReservationDate(requestData.get("reservationDate"));
            newReservation.setMemo(requestData.get("name"));
            newReservation.setEmail(requestData.get("email"));
            newReservation.setPhone(requestData.get("phone"));
            newReservation.setReserPrice(requestData.get("reserPrice"));
            newReservation.setCategory(requestData.get("category"));
            newReservation.setOrderId(requestData.get("orderId"));
            newReservation.setInsertDate(String.valueOf(now));
            reservationRepository.save(newReservation);
            //email 보내기
            if(requestData.get("message")!=null&&!requestData.get("message").equals("")){
                sendService.sendNaver(requestData.get("phone"),requestData.get("message"));
            }
        }else {
            //첫 예약
            System.out.println("isNull : "+ orderId);
            mainDto.setCategory(requestData.get("category"));
            mainDto.setOrderId(requestData.get("orderId"));
            mainDto.setEmail(requestData.get("email"));
            mainDto.setPhone(requestData.get("phone"));
            mainDto.setMemo(requestData.get("name"));
            mainDto.setReservationDate(requestData.get("reservationDate"));
            mainDto.setReserPrice(requestData.get("reserPrice"));
            mainDto.setInsertDate(String.valueOf(now));
            reservationRepository.save(mainDto.toReserBuilder());
            if(requestData.get("message")!=null&&!requestData.get("message").equals("")){
                sendService.sendNaver(requestData.get("phone"),requestData.get("message"));
            }
        }

    }
    public List<Reservation> getReservation(String category) {
        if(category==null||category.equals("")) {
            return reservationRepository.findAll();
        }
        return reservationRepository.findByCategory(category);
    }
    public List<Member> getReservation2(String category) {
        if(category==null||category.equals("")) {
//            List<Reservation> reserList = reservationRepository.findAll();
            return memberRepository.findAll();
        }
        return memberRepository.findByCategory(category);
    }
    public List<Main> getMenuList() {
        return mainMapper.getMenuList();
    }

    //예약수정 기능
    @Transactional
    public void updateReservation(Map<String,String> requestData) throws MessagingException, IOException {
        Timestamp nowTimestamp = new Timestamp(System.currentTimeMillis());
        String nowTime =nowTimestamp.toString();
        String seq = requestData.get("seq");
        String reservationDate = requestData.get("reservationDate");
        Reservation reservation = reservationRepository.findBySeq(seq);
        String oldDate = reservation.getReservationDate();
        String orderId = reservation.getOrderId();
        String email = reservation.getEmail();
        reservation.setReservationDate(reservationDate);
        reservation.setUpdateDate(nowTime);
        reservationRepository.save(reservation);
        //TODO 메신져 전송 필요
        String formattedReservationDate = "<span style=\"color: blue;\">" + reservationDate + "</span>";
        requestData.put("title",email+"님 ["+orderId+"] 예약이 변경되었습니다.");
        requestData.put("content","["+orderId+"] 이 예약이 "+reservationDate+"로 변경되었습니다.\n 이전 날짜 : "+oldDate);
        requestData.put("email",email);
        mailSetup(requestData);
    }
    public void mailSetup(Map<String,String> mailData) throws MessagingException, IOException {
        EmailDto emailDto = new EmailDto();
        emailDto.setAddress(mailData.get("email"));
        emailDto.setPhone(mailData.get("phone"));
        emailDto.setTitle(mailData.get("title"));
        emailDto.setContent(mailData.get("content"));
        sendService.sendMultipleMessage(emailDto);

    }

}
