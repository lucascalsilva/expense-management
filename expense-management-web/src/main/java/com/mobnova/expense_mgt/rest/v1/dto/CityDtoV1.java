package com.mobnova.expense_mgt.rest.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityDtoV1 {

    @NotNull
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
