package com.mever.api.domain.mainAdmin.service;

import com.mever.api.domain.mainAdmin.dto.MainDto;
import com.mever.api.domain.mainAdmin.entity.MainTitle;
import com.mever.api.domain.mainAdmin.repository.MainTitleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainTitleService {
    private final MainTitleRepository mainTitleRepository;
    private final MainDto mainDto;

    @Transactional
    public MainDto getMainTitle(String category){
        MainDto mainDto = new MainDto();
        String title="";
        String subTitle="";
        MainTitle mainTitle = mainTitleRepository.findByCategory(category);
        if(mainTitle == null || mainTitle.getCategory() == null){
            return null;
        }else {
            title = mainTitle.getTitle();
            subTitle = mainTitle.getSubTitle();
        }
        mainDto.setTitle(title);
        mainDto.setSubTitle(subTitle);
        return mainDto;
    }
    public void updateTitle(Map<String,String> requestData){
        String category = requestData.get("category");
        MainTitle mainTitle = mainTitleRepository.findByCategory(category);
        if (mainTitle == null){
            mainDto.setCategory(requestData.get("category"));
            mainDto.setTitle(requestData.get("mainTitle"));
            mainDto.setSubTitle(requestData.get("subTitle"));
            mainTitleRepository.save(mainDto.toMainTitleBuilder());
        }else {
            mainTitle.setTitle(requestData.get("mainTitle"));
            mainTitle.setSubTitle(requestData.get("subTitle"));
            mainTitleRepository.save(mainTitle);
        }
    }
}
