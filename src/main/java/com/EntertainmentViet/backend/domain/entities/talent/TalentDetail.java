package com.EntertainmentViet.backend.domain.entities.talent;

import com.EntertainmentViet.backend.domain.entities.UserDetail;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@TypeDef(name = "list-array",typeClass = ListArrayType.class)
public class TalentDetail extends UserDetail {

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  private Talent talent;

  // KYC data
  private String lastName;

  private String firstName;

  private String citizenId;

  // TODO storage for image of citizen paper
  @Type(type = "list-array")
  @Column(
      name = "citizen_paper",
      columnDefinition = "text[]"
  )
  private List<String> citizenPaper;

  public TalentDetail updateBasicInfo(TalentDetail newData) {
    super.updateBasicInfo(newData);

    return this;
  }

  public TalentDetail updateKycInfo(TalentDetail newData) {
    super.updateKycInfo(newData);
    if (newData.getLastName() != null) {
      setLastName(newData.getLastName());
    }
    if (newData.getFirstName() != null) {
      setFirstName(newData.getFirstName());
    }
    if (newData.getCitizenId() != null) {
      setCitizenId(newData.getCitizenId());
    }
    if (newData.getCitizenPaper() != null) {
      if (getCitizenPaper() == null) {
        setCitizenPaper(new ArrayList<>());
      }
      getCitizenPaper().addAll(newData.getCitizenPaper());
    }

    return this;
  }

  @Override
  public boolean isAllKycFilled() {
    if (!super.isAllKycFilled()) {
      return false;
    }

    if (lastName == null) {
      return false;
    }
    if (firstName == null) {
      return false;
    }
    if (citizenId == null) {
      return false;
    }
    if (citizenPaper == null || citizenPaper.isEmpty()) {
      return false;
    }
    return true;
  }
}
