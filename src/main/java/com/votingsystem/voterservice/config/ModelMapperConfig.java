package com.votingsystem.voterservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
  /**
   * Bean to create an object of Mode Mapper to later use it for mapping DTOs into entities and vice versa
   *
   * @return ModelMapper object
   */
  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper;
  }
}