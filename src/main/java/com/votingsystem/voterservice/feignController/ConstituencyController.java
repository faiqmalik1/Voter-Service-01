package com.votingsystem.voterservice.feignController;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import resources.constituency.ConstituencyResponseDTO;
import resources.constituency.PoolingPageResponseDTO;
import resources.constituency.PoolingResponseDTO;

@FeignClient(name = "CONSTITUENCY-SERVICE")
public interface ConstituencyController {

  /**
   * to retrieve constituency by id
   *
   * @param constituencyId:id of constituency
   * @return :return constituency if found else return null
   */
  @GetMapping("/constituency/id/{constituencyId}")
  public ConstituencyResponseDTO retrieveConstituencyById(@PathVariable(name = "constituencyId") long constituencyId);

  /**
   * retrieve all the polling
   *
   * @param pageable: to give custom size to get pageable object
   * @return :object having polling pageable object else return null
   */
  @GetMapping("/polling")
  public PoolingPageResponseDTO retrievePooling(Pageable pageable);

  /**
   * to retrieve started polling or the last polling
   *
   * @return : return the polling response else return null
   */
  @GetMapping("/polling/status")
  public PoolingResponseDTO isPoolingStarted();

}