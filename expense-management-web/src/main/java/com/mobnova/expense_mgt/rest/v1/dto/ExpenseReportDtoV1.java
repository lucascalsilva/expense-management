package com.mobnova.expense_mgt.rest.v1.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseReportDtoV1 extends BaseDtoV1 {

    @NotNull
    private String referenceID;

    @NotNull
    @Size(min = 10, max = 2000)
    private String tripDescription;

    @NotNull
    @Size(min = 10, max = 2000)
    private String justification;

    @NotNull
    private String countryCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String countryName;

    @NotNull
    private LocalDate tripStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @NotNull
    private LocalDate tripEndDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal totalAmount;
    private String creator;
    private Set<ExpenseItemDtoV1> expenses = new HashSet<ExpenseItemDtoV1>();

}
