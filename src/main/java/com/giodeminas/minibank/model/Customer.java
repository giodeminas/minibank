package com.giodeminas.minibank.model;

import com.giodeminas.minibank.constants.CustomerType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@EqualsAndHashCode(callSuper = false)
@Entity
@Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"upperFirstName", "upper_last_name", "phone",
        "upper_email"})})
public class Customer extends MiniBankRecord {

  @ManyToOne(fetch = FetchType.LAZY)
  private Account account;

  @Column
  private CustomerType type;

  @Column
  private String firstName;

  @Column
  private String lastName;

  @Column
  private String phone;

  @Column
  private String email;

  @Column(name = "upper_first_name", nullable = false)
  private String upperFirstName;

  @Column(name = "upper_last_name", nullable = false)
  private String upperLastName;

  @Column(name = "upper_email", nullable = false)
  private String upperEmail;

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Address> addresses;

  public Customer(Account account, CustomerType type, String firstName, String lastName,
      String phone, String email) {
    this.account = account;
    this.type = type;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.email = email;
  }

  @PrePersist
  @PreUpdate
  public void updateUpperCaseFields() {
    this.upperFirstName = firstName != null ? firstName.toUpperCase() : null;
    this.upperLastName = lastName != null ? lastName.toUpperCase() : null;
    this.upperEmail = email != null ? email.toUpperCase() : null;
  }
}
