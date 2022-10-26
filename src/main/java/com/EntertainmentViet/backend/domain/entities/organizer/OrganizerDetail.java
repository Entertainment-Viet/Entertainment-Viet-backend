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

  // TODO storage for image of citizen paper
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
      if (getBusinessPaper() == null) {
        setBusinessPaper(new ArrayList<>());
      }
      getBusinessPaper().addAll(newData.getBusinessPaper());
    }
    return this;
  }
}
