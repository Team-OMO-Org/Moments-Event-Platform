package io.github.teamomo.momentswebapp.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageResponse<T> extends PageImpl<T> {

  @JsonCreator
  public PageResponse(
      @JsonProperty("content") List<T> content,
      @JsonProperty("page") PageInfo pageInfo
  ) {
    super(content, PageRequest.of(pageInfo.getNumber(), pageInfo.getSize()), pageInfo.getTotalElements());
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class PageInfo {
    private int size;
    private int number;
    private long totalElements;
    private int totalPages;

    @JsonCreator
    public PageInfo(
        @JsonProperty("size") int size,
        @JsonProperty("number") int number,
        @JsonProperty("totalElements") long totalElements,
        @JsonProperty("totalPages") int totalPages
    ) {
      this.size = size;
      this.number = number;
      this.totalElements = totalElements;
      this.totalPages = totalPages;
    }

    public int getSize() {
      return size;
    }

    public int getNumber() {
      return number;
    }

    public long getTotalElements() {
      return totalElements;
    }

    public int getTotalPages() {
      return totalPages;
    }
  }
}