package com.mever.api.domain.analytics.controller;

import com.mever.api.domain.analytics.AnalyticsReporting;
import com.mever.api.domain.analytics.dto.AnalyticsDto;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.SimpleTimeZone;
import java.util.stream.Collectors;

@RestController
public class AnalyticsController {

    @PostMapping("/analyticsList")
    public ResponseEntity<List> analytics(
            @ApiParam(value = "소속 그룹", required = true) @RequestParam String group){
        List<AnalyticsDto> analyticsList = AnalyticsReporting.analyticReport();
        List<AnalyticsDto> filteredList;
        switch (group) {
            case "cafe1":
                filteredList = analyticsList.stream()
                        .filter(dto -> dto.getPage_title().equals("메버(MEVER)-선릉카페"))
                        .collect(Collectors.toList());
                break;
            case "art1":
                filteredList = analyticsList.stream()
                        .filter(dto -> dto.getPage_title().equals("메버(MEVER)-청담갤러리1"))
                        .collect(Collectors.toList());
                break;
            case "art2":
                filteredList = analyticsList.stream()
                        .filter(dto -> dto.getPage_title().equals("메버(MEVER)-청담갤러리2"))
                        .collect(Collectors.toList());
                break;
            default:
                // 그룹 값이 일치하는 경우가 없을 때 처리
                return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(filteredList);
    }
}
