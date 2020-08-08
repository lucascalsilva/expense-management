package com.mobnova.expense_mgt.util;

import com.mobnova.expense_mgt.dto.v1.BaseDto;
import com.mobnova.expense_mgt.model.BaseEntity;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

@UtilityClass
@Slf4j
public class AssertionUtils {

    public <T extends BaseEntity> void entityUpdateAssertions(T oldEntity, T newEntity) {
        assertThat(oldEntity.getId()).isEqualTo(newEntity.getId());
        assertThat(oldEntity.getCreationDate()).isEqualTo(newEntity.getCreationDate());
        assertThat(oldEntity.getVersion()).isNotEqualTo(newEntity.getVersion());
        assertThat(oldEntity.getLastUpdateDate()).isNotEqualTo(newEntity.getLastUpdateDate());
    }

    public <T extends BaseDto> void dtoUpdateAssertions(T oldDto, T newDto) {
        assertThat(oldDto.getId()).isEqualTo(newDto.getId());
        assertThat(oldDto.getCreationDate()).isEqualTo(newDto.getCreationDate());
        assertThat(oldDto.getLastUpdateDate()).isNotEqualTo(newDto.getLastUpdateDate());
    }
}
