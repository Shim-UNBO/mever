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
    @Autowired
    AnalyticsReporting analyticsReporting;

    @PostMapping("/analyticsList")
    public ResponseEntity<List> analytics(
            @ApiParam(value = "소속 그룹", required = true) @RequestParam String group){
        List<AnalyticsDto> analyticsList = analyticsReporting.analyticReport();
        List<AnalyticsDto> filteredList;
        switch (group) {
            case "cafe1":
                filteredList = analyticsList.stream()
                        .filter(dto -> dto.getPath_url().equals("/cafe1/"))
                        .collect(Collectors.toList());
                break;
            case "art1":
                filteredList = analyticsList.stream()
                        .filter(dto -> dto.getPath_url().equals("/art1/"))
                        .collect(Collectors.toList());
                break;
            case "art2":
                filteredList = analyticsList.stream()
                        .filter(dto -> dto.getPath_url().equals("/art2/"))
                        .collect(Collectors.toList());
                break;
            case "hospital1":
                filteredList = analyticsList.stream()
                        .filter(dto -> dto.getPath_url().equals("/hospital1/"))
                        .collect(Collectors.toList());
                break;
            default:
                // 그룹 값이 일치하는 경우가 없을 때 처리
                return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(filteredList);
    }
}
