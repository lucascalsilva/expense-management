package com.mobnova.expense_mgt.dto.v1;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class NameCodeDto extends BaseDto {

    @NotBlank
    private String code;

    @NotBlank
    private String name;
}
