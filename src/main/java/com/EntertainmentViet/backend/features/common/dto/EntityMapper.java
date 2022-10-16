package com.EntertainmentViet.backend.features.common.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

@Mapper(config = MappingConfig.class)
public class EntityMapper {

  @Autowired
  private TalentRepository talentRepository;

  @Autowired
  private OrganizerRepository organizerRepository;

  @ToTalentUid
  public UUID toTalentUid(Talent talent) {
    return talent != null ? talent.getUid() : null;
  }

  @ToTalentEntity
  public Talent toTalentEntity(UUID talentUid) {
    return talentRepository.findByUid(talentUid).orElse(null);
  }

  @ToTalentName
  public String toTalentName(Talent talent) {
    return talent != null ? talent.getDisplayName() : null;
  }

  @ToOrganizerUid
  public UUID toOrganizerUid(Organizer organizer) {
    return organizer != null ? organizer.getUid() : null;
  }

  @ToOrganizerEntity
  public Organizer toOrganizerEntity(UUID organizerUid) {
    return organizerRepository.findByUid(organizerUid).orElse(null);
  }

  @ToOrganizerName
  public String toOrganizerName(Organizer organizer) {
    return organizer != null ? organizer.getDisplayName() : null;
  }

  @ToPackageUid
  public UUID toPackageUid(Package talentPackage) {
    return talentPackage != null ? talentPackage.getUid() : null;
  }

  @ToPackageName
  public String toPackageName(Package talentPackage) {
    return talentPackage != null ? talentPackage.getName() : null;
  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToTalentUid { }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToTalentEntity { }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToTalentName { }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToOrganizerUid { }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToOrganizerEntity { }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToOrganizerName { }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToPackageUid { }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToPackageName { }
}
