package com.votingsystem.voterservice.controller;

import resources.ResponseDTO;
import resources.voter.VoterPageResponseDTO;
import com.votingsystem.voterservice.service.VoterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import resources.voter.VoterRequestDTO;
import resources.voter.VoterResponseDTO;


@RestController
@RequiredArgsConstructor
@RequestMapping("/voter")
public class VoterController {

  private final VoterService voterService;

  /**
   * to create the voter
   *
   * @param voterRequestDTO: contains the user id and constituency id
   * @return : return success if created successfully else return failure exception
   */
  @PostMapping("")
  public ResponseDTO createVoter(@RequestBody VoterRequestDTO voterRequestDTO) {
    return voterService.createVoter(voterRequestDTO);
  }

  /**
   * to retrieve voters in constituency
   *
   * @param token:          to validate user
   * @param constituencyId: constituency id
   * @param pageable:       to give custom size
   * @return :return pageable object of voter response else return null
   */
  @GetMapping("/all/{constituencyId}")
  public VoterPageResponseDTO retrieveAllVotersInConstituency(@CookieValue("Authorization") String token, @PathVariable(name = "constituencyId") long constituencyId, Pageable pageable) {
    return voterService.retrieveVotersInConstituency(token, constituencyId, pageable);
  }

  /**
   * check if voter is registered in constituency or not
   *
   * @param token:   to validate request
   * @param voterId: voter id
   * @return : return the voter response if voter is present else return null
   */
  @GetMapping("/{voterId}")
  public VoterResponseDTO isVoterRegisteredInConstituency(@CookieValue("Authorization") String token, @PathVariable(name = "voterId") long voterId) {
    return voterService.isVoterRegisteredInConstituency(token, voterId);
  }

  /**
   * count all voter in constituency
   *
   * @param constituencyId: id of constituency
   * @return : count of voter
   */
  @GetMapping("/{constituencyId}/count")
  public long countAllVoterInConstituency(@PathVariable long constituencyId) {
    return voterService.countAllVoterInConstituency(constituencyId);
  }

  /**
   * get the constituency of voter
   *
   * @param voterId: if of voter
   * @return :return the voter response if found else return null
   */
  @GetMapping("/{voterId}/constituency")
  public VoterResponseDTO getVoterConstituency(@PathVariable(name = "voterId") long voterId) {
    return voterService.getVoterConstituency(voterId);
  }


}