package com.mobnova.expense_mgt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto extends BaseDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;
}
