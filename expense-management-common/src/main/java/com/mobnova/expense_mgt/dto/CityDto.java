package com.mobnova.expense_mgt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CityDto extends BaseDto {

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
