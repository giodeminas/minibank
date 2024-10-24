package com.giodeminas.minibank.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageinatedResponse<T> {

  private List<T> content;
  private int totalPages;
  private long totalElements;

  //TODO add search query input to pages
}
