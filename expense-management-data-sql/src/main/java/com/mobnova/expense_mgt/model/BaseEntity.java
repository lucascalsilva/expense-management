package com.mobnova.expense_mgt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    @Setter
    private Long id;

    @Column(name="CREATION_DATE", updatable = false, nullable = false)
    private LocalDateTime creationDate;

    @Column(name="LAST_UPDATE_DATE", nullable = false)
    private LocalDateTime lastUpdateDate;

    @Version
    @Column(name="VERSION")
    @Setter
    private Integer version;

    @PrePersist
    public void onPrePersist(){
        this.creationDate = LocalDateTime.now();
        this.lastUpdateDate = LocalDateTime.now();
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

        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 13;
    }
}
