package com.mobnova.expense_mgt.util;

import com.mobnova.expense_mgt.model.BaseEntity;
import lombok.experimental.UtilityClass;

import static org.assertj.core.api.Assertions.assertThat;

@UtilityClass
public class AssertionsUtil {

    public <T extends BaseEntity> void entityUpdateAssertions(T oldEntity, T newEntity){
        assertThat(oldEntity.getId()).isEqualTo(newEntity.getId());
        assertThat(oldEntity.getCreationDate()).isEqualTo(newEntity.getCreationDate());
        assertThat(oldEntity.getVersion()).isNotEqualTo(newEntity.getVersion());
        assertThat(oldEntity.getLastUpdateDate()).isNotEqualTo(newEntity.getLastUpdateDate());
    }
}
