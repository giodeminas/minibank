package com.giodeminas.minibank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDTOPaginated {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long rowNumber;
  private CustomerDTO customer;
}
