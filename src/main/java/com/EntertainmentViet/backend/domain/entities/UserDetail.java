package com.EntertainmentViet.backend.domain.entities;

import com.EntertainmentViet.backend.domain.values.Location;
import com.EntertainmentViet.backend.domain.values.Location_;
import com.EntertainmentViet.backend.domain.values.UserInputText;
import com.EntertainmentViet.backend.domain.values.UserInputText_;
import com.fasterxml.jackson.databind.JsonNode;
import com.querydsl.core.annotations.QueryInit;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@TypeDef(name = "list-array",typeClass = ListArrayType.class)
public abstract class UserDetail {

  @Id
  private Long id;

  private String avatar;

  @Type(type = "list-array")
  @Column(
      name = "description_img",
      columnDefinition = "text[]"
  )
  private List<String> descriptionImg;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride( name = UserInputText_.INPUT_LANG, column = @Column(name = UserDetail_.BIO + "_" + UserInputText_.INPUT_LANG)),
      @AttributeOverride( name = UserInputText_.RAW_INPUT, column = @Column(name = UserDetail_.BIO + "_" + UserInputText_.RAW_INPUT)),
      @AttributeOverride( name = UserInputText_.INPUT_TRANSLATION, column = @Column(name = UserDetail_.BIO + "_" + UserInputText_.INPUT_TRANSLATION))
  })
  private UserInputText bio;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private JsonNode extensions;

  // KYC data
  private String phoneNumber;


  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "location_id", referencedColumnName = Location_.ID)
  @QueryInit("*.*")
  private Location address;

  private String taxId;

  private String bankAccountNumber;

  private String bankAccountOwner;

  private String bankName;

  private String bankBranchName;

  public UserDetail updateBasicInfo(UserDetail newData) {
    if (newData.getBio() != null) {
      setBio(newData.getBio());
    }
    if (newData.getExtensions() != null) {
      setExtensions(newData.getExtensions());
    }
    if (newData.getDescriptionImg() != null) {
      setDescriptionImg(newData.getDescriptionImg());
    }
    if (newData.getAvatar() != null) {
      setAvatar(newData.getAvatar());
    }
    return this;
  }

  public UserDetail updateKycInfo(UserDetail newData) {
    if (newData.getPhoneNumber() != null) {
      setPhoneNumber(newData.getPhoneNumber());
    }
    if (newData.getAddress() != null) {
      setAddress(newData.getAddress());
    }
    if (newData.getTaxId() != null) {
      setTaxId(newData.getTaxId());
    }
    if (newData.getBankAccountNumber() != null) {
      setBankAccountNumber(newData.getBankAccountNumber());
    }
    if (newData.getBankAccountOwner() != null) {
      setBankAccountOwner(newData.getBankAccountOwner());
    }
    if (newData.getBankName() != null) {
      setBankName(newData.getBankName());
    }
    if (newData.getBankBranchName() != null) {
      setBankBranchName(newData.getBankBranchName());
    }

    return this;
  }

  public boolean isAllKycFilled() {
    if (phoneNumber == null) {
      return false;
    }
    if (address == null) {
      return false;
    }
    if (taxId == null) {
      return false;
    }
    if (bankAccountNumber == null) {
      return false;
    }
    if (bankAccountOwner == null) {
      return false;
    }
    if (bankName == null) {
      return false;
    }
    if (bankBranchName == null) {
      return false;
    }
    return true;
  }
}
