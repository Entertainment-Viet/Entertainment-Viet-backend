/*
 * Author : AdNovum Informatik AG
 */

package com.EntertainmentViet.backend.domain.values;

import java.io.Serializable;
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
public class LocationType implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private String type;

	@NotNull
	private Integer level;

}
