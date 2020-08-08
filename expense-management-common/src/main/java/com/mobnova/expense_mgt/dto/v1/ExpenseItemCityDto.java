package com.mobnova.expense_mgt.dto.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class ExpenseItemCityDto extends BaseDto {

    @NotBlank
    private String cityCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String cityName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String countyCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String countyName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String stateOrProvinceCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String stateOrProvinceName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String countryCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String countryName;
}
