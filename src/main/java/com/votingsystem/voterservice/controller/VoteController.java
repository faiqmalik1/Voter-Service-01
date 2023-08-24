package com.votingsystem.voterservice.controller;

import com.votingsystem.voterservice.service.VotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import resources.ResponseDTO;
import resources.voter.VoteRequestDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/votes")
public class VoteController {

  private final VotesService votesService;

  /**
   * to cast the vote
   *
   * @param voteRequestDTO: contains the resource to cast vote
   * @return : return success response if vote cast successfully else return failure exception
   */
  @PostMapping("")
  public ResponseDTO castVote(@CookieValue("Authorization") String token, @RequestBody VoteRequestDTO voteRequestDTO) {
    return votesService.castVote(token, voteRequestDTO);
  }

  /**
   * get the count of votes of single candidate
   *
   * @param token:token  of user to be validated
   * @param candidateId: id of candidate whose votes to be retrieve
   * @param pollingId:   id of polling
   * @return : return the count of votes
   */
  @GetMapping("/get")
  public long retrieveCounts(@CookieValue("Authorization") String token, @RequestParam long candidateId, @RequestParam long pollingId) {
    return votesService.countVotesByCandidateIdAndPoolingId(token, candidateId, pollingId);
  }

  @GetMapping("/vote/cast")
  public boolean isVoteCasted(@RequestParam long voterId, @RequestParam long pollingId) {
    return votesService.isVoteCasted(voterId, pollingId);
  }

}