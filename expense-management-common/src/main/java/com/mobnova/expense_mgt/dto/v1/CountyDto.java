package com.mobnova.expense_mgt.dto.v1;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class CountyDto extends NameCodeDto {

    @NotNull
    private StateOrProvinceDto stateOrProvince;
}
