package com.mever.api.domain.mainAdmin.service;

import com.mever.api.domain.mainAdmin.dto.MainDto;
import com.mever.api.domain.mainAdmin.entity.ItemContents;
import com.mever.api.domain.mainAdmin.entity.MainTitle;
import com.mever.api.domain.mainAdmin.entity.Reservation;
import com.mever.api.domain.mainAdmin.repository.ItemContentsRepository;
import com.mever.api.domain.mainAdmin.repository.MainMapper;
import com.mever.api.domain.mainAdmin.repository.MainRepository;
import com.mever.api.domain.mainAdmin.repository.ReservationRepository;
import com.sun.tools.javac.Main;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
//        List<MainTitle> mainTitles = mainTitleRepository.findByCategoryOrderBySeqDesc(category);
//        MainTitle mainTitle;
//        if (mainTitles == null && mainTitles.size() > 0){
            Date now = new Date();
            String nowTime = now.toString();
            mainDto.setCategory(requestData.get("category"));
            mainDto.setTitle(requestData.get("mainTitle"));
            mainDto.setSubTitle(requestData.get("subTitle"));
            mainDto.setInsertDate(nowTime);
            mainTitleRepository.save(mainDto.toMainTitleBuilder());
//        }else {
//            mainTitle = mainTitles.get(0);
//            mainTitle.setTitle(requestData.get("mainTitle"));
//            mainTitle.setSubTitle(requestData.get("subTitle"));
//            mainTitleRepository.save(mainTitle);
//        }
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
        List<Reservation> reservations = reservationRepository.findByOrderId(orderId);
        //예약이 된 item이 존재할때
        if (!reservations.isEmpty()) {
            //예약 상태 변경
            if (requestData.get("status") != null && requestData.get("status") != "") {
                String seq =requestData.get("seq");
                Reservation reservation = reservationRepository.findBySeq(seq);
                reservation.setStatus(requestData.get("status"));
                reservationRepository.save(reservation);
                return;
            }
            //item 예약 중복 검사 후 예약넣기
            for (Reservation reservation : reservations) {
                if (reservation.getReservationDate().equals(reserDate)) {
                    throw new Exception("이미 예약된 상품입니다.");
                }
            }
            Reservation newReservation = new Reservation();
            newReservation.setReservationDate(requestData.get("reservationDate"));
            newReservation.setMemo(requestData.get("memo"));
            newReservation.setEmail(requestData.get("email"));
            newReservation.setPhone(requestData.get("phone"));
            newReservation.setReserPrice(requestData.get("reserPrice"));
            newReservation.setCategory(requestData.get("category"));
            newReservation.setOrderId(requestData.get("orderId"));
            newReservation.setInsertDate(String.valueOf(now));
            reservationRepository.save(newReservation);
        }else {
            //첫 예약
            System.out.println("isNull : "+ orderId);
            mainDto.setCategory(requestData.get("category"));
            mainDto.setOrderId(requestData.get("orderId"));
            mainDto.setEmail(requestData.get("email"));
            mainDto.setPhone(requestData.get("phone"));
            mainDto.setMemo(requestData.get("memo"));
            mainDto.setReservationDate(requestData.get("reservationDate"));
            mainDto.setReserPrice(requestData.get("reserPrice"));
            mainDto.setInsertDate(String.valueOf(now));
            reservationRepository.save(mainDto.toReserBuilder());
        }
    }
    public List<Reservation> getReservation(String category) {
        System.out.println(category);
        if(category==null||category.equals("")) {
            List<Reservation> reserList = reservationRepository.findAll();
            for (int i=0; i < reserList.size();i++){

            }
            return reservationRepository.findAll();
        }
        return reservationRepository.findByCategory(category);
    }
    public List<Main> getMenuList() {
        return mainMapper.getMenuList();
    }

}
