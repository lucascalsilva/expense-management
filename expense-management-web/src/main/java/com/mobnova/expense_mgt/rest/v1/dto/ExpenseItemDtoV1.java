package com.mobnova.expense_mgt.rest.v1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseItemDtoV1 extends BaseDtoV1 {

    @NotNull
    private Long expenseItemNumber;

    @Positive
    private BigDecimal amount;

    @NotNull
    private String currencyCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String currencyName;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate expenseDate;

    @NotNull
    private String expenseCategoryCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String expenseCategoryName;

    @NotNull
    private CityDtoV1 expenseCity;
    private Map<String, String> codeCombinations;

    @NotNull
    private String picture;
}
