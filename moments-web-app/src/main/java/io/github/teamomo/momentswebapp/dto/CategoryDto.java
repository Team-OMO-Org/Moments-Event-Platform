package io.github.teamomo.momentswebapp.dto;

public record CategoryDto(

  Long categoryId,
  String categoryName,
  Long momentCount,
  String url
){}
