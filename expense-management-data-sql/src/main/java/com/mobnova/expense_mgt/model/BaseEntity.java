package com.mobnova.expense_mgt.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@Data
public class BaseEntity {

    @Id
    @GeneratedValue
    @Column(name="ID")
    private Long id;

    @Column(name="CREATION_DATE", updatable = false)
    private LocalDateTime creationDate;

    @Column(name="LAST_UPDATE_DATE", insertable = false)
    private LocalDateTime lastUpdateDate;

    @Version
    @Column(name="VERSION")
    private Integer version;

    @PrePersist
    public void onPrePersist(){
        this.creationDate = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate(){
        this.lastUpdateDate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 13;
    }
}
