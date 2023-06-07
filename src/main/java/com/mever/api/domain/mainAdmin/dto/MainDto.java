package com.mever.api.domain.mainAdmin.dto;

import com.mever.api.domain.mainAdmin.entity.MainTitle;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class MainDto {
    @ApiModelProperty("")
    private String title;
    @ApiModelProperty("")
    private String subTitle;
    @ApiModelProperty("")
    private String category;
    @ApiModelProperty("")
    private String updateDate;
    @ApiModelProperty("")
    private String temp1;
    public MainTitle toMainTitleBuilder(){
        return MainTitle.builder()
                .title(title)
                .subTitle(subTitle)
                .category(category)
                .build();
    }
}

