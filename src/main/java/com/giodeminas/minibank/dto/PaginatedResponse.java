package com.giodeminas.minibank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponse<T> {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String query;
  private List<T> content;
  private int totalPages;
  private long totalElements;
}
