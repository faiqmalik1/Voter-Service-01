package com.votingsystem.voterservice.service;

import CustomException.CommonException;
import com.votingsystem.voterservice.feignController.CandidateController;
import com.votingsystem.voterservice.feignController.ConstituencyController;
import com.votingsystem.voterservice.feignController.UserController;
import com.votingsystem.voterservice.model.Voter;
import com.votingsystem.voterservice.model.Votes;
import com.votingsystem.voterservice.repository.VoterRepository;
import com.votingsystem.voterservice.repository.VotesRepository;
import constants.ReturnMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import resources.BaseService;
import resources.ResponseDTO;
import resources.candidate.CandidateBasicResponseDTO;
import resources.candidate.CandidateResponseDTO;
import resources.constituency.PoolingResponseDTO;
import resources.user.UserResponseDTO;
import resources.user.ValidateResponseDTO;
import resources.voter.VoteRequestDTO;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VotesService extends BaseService {

  private final VotesRepository votesRepository;
  private final VoterRepository voterRepository;
  private final CandidateController candidateController;
  private final ConstituencyController constituencyController;
  private final UserController userController;

  /**
   * to cast the vote
   *
   * @param voteRequestDTO: contains the resource to cast vote
   * @return : return success response if vote cast successfully else return failure exception
   */
  public ResponseDTO castVote(String token, VoteRequestDTO voteRequestDTO) {
    ValidateResponseDTO validateResponseDTO = userController.validateToken(token);
    CandidateBasicResponseDTO candidateResponse = candidateController.retrieveSCandidate(voteRequestDTO.getCandidateId());
    if (candidateResponse == null) {
      throw new CommonException(ReturnMessage.CANDIDATE_NOT_EXISTS.getValue(), HttpStatus.BAD_REQUEST);
    }
    Optional<Voter> optionalVoter = voterRepository.findByVoterIdAndConstituencyId(voteRequestDTO.getVoterId(), candidateResponse.getConstituencyId());
    if (optionalVoter.isEmpty()) {
      throw new CommonException(ReturnMessage.INVALID_HALKA.getValue(), HttpStatus.BAD_REQUEST);
    }
    PoolingResponseDTO poolingResponseDTO = constituencyController.isPoolingStarted();
    if (poolingResponseDTO.getPoolingId() != voteRequestDTO.getPoolingId()) {
      throw new CommonException(ReturnMessage.POLLING_NOT_STARTED.getValue(), HttpStatus.BAD_REQUEST);
    }
    Optional<Votes> optionalVotes = votesRepository.findByVoterIdAndPoolingId(optionalVoter.get(), voteRequestDTO.getPoolingId());
    if (optionalVotes.isPresent()) {
      throw new CommonException(ReturnMessage.VOTER_ALREADY_CASTED.getValue(), HttpStatus.BAD_REQUEST);
    }
    UserResponseDTO userResponseDTO = userController.getUser(token, validateResponseDTO.getUserId());
    CandidateResponseDTO candidateResponseDTO = candidateController.retrieveCandidateDetail(token, candidateResponse.getCandidateId());
    Votes vote = new Votes();
    vote.setCandidateId(voteRequestDTO.getCandidateId());
    vote.setPoolingId(voteRequestDTO.getPoolingId());
    vote.setVoterId(optionalVoter.get());
    votesRepository.save(vote);
    String body = "Greetings " + userResponseDTO.getName() + "\nThank you for casting vote." + "\nFollowing are casted vote details\nPolling Id: " + poolingResponseDTO.getPoolingId() + "\nVote Casted To: " + candidateResponseDTO.getName() + "\nCandidate Party Name: " + candidateResponseDTO.getPartyName() + "\nHalka Name: " + candidateResponseDTO.getConstituencyName() + "\n\nRegards\nVoting Team";
    userController.sendEmail(token, body);
    return generateSuccessResponse();
  }

  /**
   * get the count of votes of single candidate
   *
   * @param token:token  of user to be validated
   * @param candidateId: id of candidate whose votes to be retrieve
   * @param pollingId:   id of polling
   * @return : return the count of votes
   */
  public long countVotesByCandidateIdAndPoolingId(String token, long candidateId, long pollingId) {
    return votesRepository.countVotesByCandidateIdAndPoolingId(candidateId, pollingId);
  }

  public boolean isVoteCasted(long voterId, long pollingId) {
    Optional<Voter> optionalVoter = voterRepository.findById(voterId);
    if (optionalVoter.isEmpty()) {
      return false;
    }
    Optional<Votes> optionalVotes = votesRepository.findByVoterIdAndPoolingId(optionalVoter.get(), pollingId);
    return optionalVotes.isPresent();
  }
}