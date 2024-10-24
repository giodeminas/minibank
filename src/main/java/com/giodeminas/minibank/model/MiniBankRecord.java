package com.giodeminas.minibank.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
//TODO make us of Hibernate Envers
public abstract class MiniBankRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Version
  Long versionNum;

  @CreatedBy
  String createdBy;

  @Column(nullable = false, updatable = false)
  LocalDateTime creationDate;

  @LastModifiedBy
  String lastModifiedBy;

  @Column(nullable = false)
  LocalDateTime lastModifiedDate;

  @PrePersist
  protected void onCreate() {
    this.creationDate = LocalDateTime.now();
    this.lastModifiedDate = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.lastModifiedDate = LocalDateTime.now();
  }
}
