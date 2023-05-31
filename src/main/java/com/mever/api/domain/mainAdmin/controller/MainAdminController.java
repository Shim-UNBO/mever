package com.mever.api.domain.mainAdmin.controller;


import com.mever.api.domain.mainAdmin.dto.MainDto;
import com.mever.api.domain.mainAdmin.service.MainTitleService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainAdminController {
    @Autowired
    private MainTitleService mainTitleService;

    @PostMapping("/getMainTitle")
    public ResponseEntity<MainDto> getMainTitle(
            @RequestBody Map<String,String> requestData
    ){
        String category = requestData.get("category");
        return ResponseEntity.ok(mainTitleService.getMainTitle(category));
    }
}
