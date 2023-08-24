package com.votingsystem.voterservice.feignController;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import resources.candidate.CandidateBasicResponseDTO;
import resources.candidate.CandidateResponseDTO;

@FeignClient(name = "CANDIDATE-SERVICE")
public interface CandidateController {

  /**
   * it will retrieve candidate with its details
   *
   * @param candidateId: id of candidate
   * @return :return candidate response if candidate present else return null
   */
  @GetMapping("/candidate/{candidateId}")
  public CandidateResponseDTO retrieveCandidateDetail(@CookieValue("Authorization") String token, @PathVariable(name = "candidateId") long candidateId);


  /**
   * to retrieve the basic info of candidate
   *
   * @param candidateId: id of candidate
   * @return :return the candidate basic response else return null
   */
  @GetMapping("/candidate/{candidateId}/get")
  public CandidateBasicResponseDTO retrieveSCandidate(@PathVariable(name = "candidateId") long candidateId);

}
