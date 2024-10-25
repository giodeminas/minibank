package com.giodeminas.minibank.repository;

import com.giodeminas.minibank.constants.CustomerType;
import com.giodeminas.minibank.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

  @Query("SELECT c FROM Customer c LEFT JOIN c.addresses a " +
      "WHERE (:type IS NULL OR c.type = :type)" +
      "AND (:firstName IS NULL OR UPPER(c.firstName) like UPPER(CONCAT('%', :firstName, '%'))) " +
      "AND (:lastName IS NULL OR UPPER(c.lastName) like UPPER(CONCAT('%', :lastName, '%'))) " +
      "AND (:phone IS NULL OR c.phone = :phone) " +
      "AND (:email IS NULL OR UPPER(c.email) like UPPER(CONCAT('%', :email, '%'))) " +
      "AND (:house IS NULL OR UPPER(a.house) = UPPER(CONCAT('%', :house, '%'))) " +
      "AND (:street IS NULL OR UPPER(a.street) like UPPER(CONCAT('%', :street, '%'))) " +
      "AND (:city IS NULL OR UPPER(a.city) like UPPER(CONCAT('%', :city, '%')))" +
      "AND (:state IS NULL OR UPPER(a.state) like UPPER(CONCAT('%', :state, '%')))" +
      "AND (:zip IS NULL OR a.zip = :zip)" +
      "AND (:country IS NULL OR UPPER(a.country) like UPPER(CONCAT('%', :country, '%')))")
  Page<Customer> searchCustomers(
      @Param("type") CustomerType type,
      @Param("firstName") String firstName,
      @Param("lastName") String lastName,
      @Param("phone") String phone,
      @Param("email") String email,
      @Param("house") String house,
      @Param("street") String street,
      @Param("city") String city,
      @Param("state") String state,
      @Param("zip") String zip,
      @Param("country") String country,
      Pageable pageable);
}
