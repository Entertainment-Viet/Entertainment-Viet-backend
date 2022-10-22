package com.EntertainmentViet.backend.domain.entities;

import com.EntertainmentViet.backend.domain.values.UserInputText;
import com.EntertainmentViet.backend.domain.values.UserInputText_;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

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

  private String address;

  private String taxId;

  private String bankAccountNumber;

  private String bankAccountOwner;

  private String bankName;

  private String bankBranchName;

  public UserDetail updateBasicInfo(UserDetail newData) {
    if (newData.getBio() != null) {
      setBio(newData.getBio());
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
}
