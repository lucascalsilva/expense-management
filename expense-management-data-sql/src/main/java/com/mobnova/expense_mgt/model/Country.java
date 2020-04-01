package com.mobnova.expense_mgt.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@ToString(callSuper = true)
@Entity
@Table(name = "COUNTRIES")
@Data
public class Country extends NameCodeEntity {

}
