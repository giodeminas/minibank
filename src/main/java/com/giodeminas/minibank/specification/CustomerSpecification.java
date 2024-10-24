package com.giodeminas.minibank.specification;

import com.giodeminas.minibank.dto.CustomerDTO;
import com.giodeminas.minibank.model.Customer;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

//TODO add search by CustomerType, account number and address fields
public class CustomerSpecification {

  public static Specification<Customer> withFilters(CustomerDTO customerDTO) {
    return ((root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (customerDTO.getFirstName() != null && !customerDTO.getFirstName().isEmpty()) {
        predicates.add(
            criteriaBuilder.like(root.get("firstName"), "%" + customerDTO.getFirstName() + "%"));
      }
      if (customerDTO.getLastName() != null && !customerDTO.getLastName().isEmpty()) {
        predicates.add(
            criteriaBuilder.like(root.get("lastName"), "%" + customerDTO.getLastName() + "%"));
      }
      if (customerDTO.getEmail() != null && !customerDTO.getEmail().isEmpty()) {
        predicates.add(criteriaBuilder.equal(root.get("email"), customerDTO.getEmail()));
      }
      if (customerDTO.getPhone() != null && !customerDTO.getPhone().isEmpty()) {
        predicates.add(criteriaBuilder.equal(root.get("phone"), customerDTO.getPhone()));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    });
  }
}
