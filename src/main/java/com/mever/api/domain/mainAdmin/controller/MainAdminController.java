package com.mever.api.domain.mainAdmin.controller;


import com.mever.api.domain.mainAdmin.dto.MainDto;
import com.mever.api.domain.mainAdmin.service.MainAdminService;
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
    private MainAdminService mainAdminService;

    @PostMapping("/getMainTitle")
    public ResponseEntity<MainDto> getMainTitle(
            @RequestBody Map<String,String> requestData
    ){
        String category = requestData.get("category");
        return ResponseEntity.ok(mainAdminService.getMainTitle(category));
    }
    @PostMapping("/updateTitle")
    public ResponseEntity<MainDto> updateTitle(
            @RequestBody Map<String,String> requestData
    ){
        mainAdminService.updateTitle(requestData);

        return ResponseEntity.ok().build();
    }
    @PostMapping("/updateContents")
    public ResponseEntity<MainDto> updateContents(
            @RequestBody Map<String,String> requestData
    ){
        mainAdminService.updateContents(requestData);

        return ResponseEntity.ok().build();
    }
    @PostMapping("/getItemContents")
    public ResponseEntity<MainDto> getItemContents(
            @RequestBody Map<String,String> requestData
    ){
            String category = requestData.get("category");
            String orderName = requestData.get("orderName");
            return ResponseEntity.ok(mainAdminService.getItemContents(category,orderName));
    }
    @PostMapping("/itemList")
    public ResponseEntity itemList(
            @ApiParam(value = "요청 객체", required = false) @RequestParam(required = false) String category) throws Exception {
        try {
            return ResponseEntity.ok(mainAdminService.getItemList(category));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
}
