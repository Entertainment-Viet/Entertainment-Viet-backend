package com.EntertainmentViet.backend.domain.entities.admin;

import com.EntertainmentViet.backend.domain.entities.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Admin extends Account {
}
