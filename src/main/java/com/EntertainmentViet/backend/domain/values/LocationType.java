/*
 * Author : AdNovum Informatik AG
 */

package com.EntertainmentViet.backend.domain.values;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
public class LocationType {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private String type;

	@NotNull
	private String level;

}
