package com.mobnova.expense_mgt.dto.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class ExpenseItemDto extends BaseDto {

    @Positive
    private Long expenseItemNumber;

    @Positive
    private BigDecimal amount;

    @NotBlank
    private String currencyCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String currencyName;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate expenseDate;

    @NotBlank
    private String expenseCategoryCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String expenseCategoryName;

    @NotNull
    private ExpenseItemCityDto expenseCity;
    private String segmentValuePairs;

    @NotNull
    private String picture;
}
