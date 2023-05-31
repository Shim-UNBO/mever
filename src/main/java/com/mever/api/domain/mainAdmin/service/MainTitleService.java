package com.mever.api.domain.mainAdmin.service;

import com.mever.api.domain.mainAdmin.dto.MainDto;
import com.mever.api.domain.mainAdmin.entity.MainTitle;
import com.mever.api.domain.mainAdmin.repository.MainTitleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainTitleService {
    private final MainTitleRepository mainTitleRepository;

    @Transactional
    public MainDto getMainTitle(String category){
        MainDto mainDto = new MainDto();
        String title="";
        String subTitle="";
        MainTitle mainTitle = mainTitleRepository.findByCategory(category);
        if(mainTitle == null || mainTitle.getCategory() == null){
            title = "Temporary Title";
            subTitle = "Temporary Subtitle";
        }else {
            title = mainTitle.getTitle();
            subTitle = mainTitle.getSubTitle();
        }
        mainDto.setTitle(title);
        mainDto.setSub_title(subTitle);
        return mainDto;
    }
}
