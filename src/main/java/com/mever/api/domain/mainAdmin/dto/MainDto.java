package com.mever.api.domain.mainAdmin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MainDto {
    @ApiModelProperty("")
    private String title;
    @ApiModelProperty("")
    private String sub_title;
    @ApiModelProperty("")
    private String category;
    @ApiModelProperty("")
    private String update_date;
    @ApiModelProperty("")
    private String temp1;
}
