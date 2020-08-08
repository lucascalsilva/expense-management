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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class ExpenseReportDto extends BaseDto {

    @NotBlank
    private String referenceID;

    @NotBlank
    @Size(min = 3, max = 2000)
    private String tripDescription;

    @NotBlank
    @Size(min = 3, max = 2000)
    private String justification;

    @NotBlank
    private String countryCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String countryName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @NotNull
    private LocalDate tripStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @NotNull
    private LocalDate tripEndDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal totalAmount;

    @NotBlank
    private String creator;

    @NotEmpty
    private Set<ExpenseItemDto> expenses = new HashSet<ExpenseItemDto>();

}
