package com.giodeminas.minibank.entities;

import com.giodeminas.minibank.constants.CustomerType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends MiniBankRecord {

  @ManyToOne
  private Account account;

  private CustomerType type;

  private String firstName;

  private String lastName;

  private String phone;

  private String email;

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
  private List<Address> addresses = new ArrayList<Address>();
}
