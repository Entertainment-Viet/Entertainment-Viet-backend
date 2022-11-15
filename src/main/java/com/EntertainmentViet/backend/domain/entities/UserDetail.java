package com.EntertainmentViet.backend.domain.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import com.EntertainmentViet.backend.domain.values.Location;
import com.EntertainmentViet.backend.domain.values.Location_;
import com.EntertainmentViet.backend.domain.values.UserInputText;
import com.EntertainmentViet.backend.domain.values.UserInputText_;
import com.fasterxml.jackson.databind.JsonNode;
import com.querydsl.core.annotations.QueryInit;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public abstract class UserDetail {

  @Id
  private Long id;

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

  private String email;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
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
    return this;
  }

  public UserDetail updateKycInfo(UserDetail newData) {
    if (newData.getPhoneNumber() != null) {
      setPhoneNumber(newData.getPhoneNumber());
    }
    if (newData.getEmail() != null) {
      setEmail(newData.getEmail());
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
    if (email == null) {
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
