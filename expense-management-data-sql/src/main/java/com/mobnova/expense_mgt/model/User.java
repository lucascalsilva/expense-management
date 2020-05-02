package com.mobnova.expense_mgt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@ToString(callSuper = true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class User extends BaseEntity{

    @Column(name = "FIRST_NAME")
    @NotNull
    private String firstName;

    @Column(name = "LAST_NAME")
    @NotNull
    private String lastName;

    @Column(name = "USERNAME")
    @NaturalId
    @NotNull
    private String username;

    @Column(name = "PASSWORD")
    @NotNull
    private String password;

    @Column(name = "EMAIL")
    @NaturalId
    @NotNull
    @Email
    private String email;

}
