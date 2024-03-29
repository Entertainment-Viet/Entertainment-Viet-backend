package com.EntertainmentViet.backend.domain.entities.organizer;

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
public class OrganizerDetail extends UserDetail {

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  private Organizer organizer;

  // KYC data
  private String companyName;

  private String representative;

  private String position;

  @Type(type = "list-array")
  @Column(
      name = "business_paper",
      columnDefinition = "text[]"
  )
  private List<String> businessPaper;

  public OrganizerDetail updateBasicInfo(OrganizerDetail newData) {
    super.updateBasicInfo(newData);

    return this;
  }

  public OrganizerDetail updateKycInfo(OrganizerDetail newData) {
    super.updateKycInfo(newData);
    if (newData.getCompanyName() != null) {
      setCompanyName(newData.getCompanyName());
    }
    if (newData.getRepresentative() != null) {
      setRepresentative(newData.getRepresentative());
    }
    if (newData.getPosition() != null) {
      setPosition(newData.getPosition());
    }
    if (newData.getBusinessPaper() != null) {
      setBusinessPaper(newData.getBusinessPaper());
    }

    return this;
  }

  @Override
  public boolean isAllKycFilled() {
    if (!super.isAllKycFilled()) {
      return false;
    }

    if (companyName == null) {
      return false;
    }
    if (representative == null) {
      return false;
    }
    if (position == null) {
      return false;
    }
    if (businessPaper == null || businessPaper.isEmpty()) {
      return false;
    }
    return true;
  }
}
