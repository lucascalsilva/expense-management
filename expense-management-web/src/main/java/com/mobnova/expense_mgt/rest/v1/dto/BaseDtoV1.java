package com.mobnova.expense_mgt.rest.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BaseDtoV1 {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime creationDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime lastUpdateDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseDtoV1 baseDtoV1 = (BaseDtoV1) o;

        return id != null && Objects.equals(id, baseDtoV1.id);
    }

    @Override
    public int hashCode() {
        return 13;
    }
}
