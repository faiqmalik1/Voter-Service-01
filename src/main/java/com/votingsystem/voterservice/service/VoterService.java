package com.votingsystem.voterservice.service;

import CustomException.CommonException;
import com.votingsystem.voterservice.feignController.ConstituencyController;
import com.votingsystem.voterservice.feignController.UserController;
import com.votingsystem.voterservice.model.Voter;
import com.votingsystem.voterservice.repository.VoterRepository;
import com.votingsystem.voterservice.repository.VotesRepository;
import com.votingsystem.voterservice.resources.ModelToResponse;
import constants.ReturnMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import resources.ResponseDTO;
import resources.user.UserListRequestDTO;
import resources.user.UserListResponseDTO;
import resources.user.UserResponseDTO;
import resources.voter.VoterPageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import resources.BaseService;
import resources.constituency.ConstituencyResponseDTO;
import resources.voter.VoterRequestDTO;
import resources.voter.VoterResponseDTO;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoterService extends BaseService {

  private final VoterRepository voterRepository;
  private final VotesRepository votesRepository;
  private final ConstituencyController constituencyController;
  private final UserController userController;

  /**
   * to create the voter
   *
   * @param voterRequestDTO: contains the user id and constituency id
   * @return : return success if created successfully else return failure exception
   */
  public ResponseDTO createVoter(VoterRequestDTO voterRequestDTO) {
    Voter voter = new Voter();
    voter.setVoterId(voterRequestDTO.getUserId());
    ConstituencyResponseDTO response = constituencyController.retrieveConstituencyById(voterRequestDTO.getConstituencyId());
    if (response == null) {
      throw new CommonException(ReturnMessage.INVALID_CONSTITUENCY.getValue(), HttpStatus.BAD_REQUEST);
    }
    voter.setConstituencyId(voterRequestDTO.getConstituencyId());
    voterRepository.save(voter);
    return generateSuccessResponse();
  }

  /**
   * to retrieve voters in constituency
   *
   * @param token:          to validate user
   * @param constituencyId: constituency id
   * @param pageable:       to give custom size
   * @return :return pageable object of voter response else return null
   */
  public VoterPageResponseDTO retrieveVotersInConstituency(@CookieValue("token") String token, long constituencyId, Pageable pageable) {
    Page<Voter> voterPage = voterRepository.findAllByConstituencyId(constituencyId, pageable);
    if (voterPage.isEmpty()) {
      return new VoterPageResponseDTO(null);
    }
    List<Long> userIdList = voterPage.getContent()
            .stream()
            .map(Voter::getVoterId).toList();
    UserListRequestDTO userListRequestDTO = new UserListRequestDTO(userIdList);
    UserListResponseDTO userResponseList = userController.findUsers(token, userListRequestDTO);
    Page<VoterResponseDTO> voterResponsePage = voterPage.map(voter -> {
      Optional<UserResponseDTO> matchingUserResponse = userResponseList.getUserResponseDTOList().stream()
              .filter(userResponse -> userResponse.getId() == voter.getVoterId())
              .findFirst();
      return matchingUserResponse.map(userResponse ->
              ModelToResponse.parseVoterToVoterResponse(voter, userResponse)
      ).orElse(null);
    });
    return new VoterPageResponseDTO(voterResponsePage);
  }

  /**
   * check if voter is registered in constituency or not
   *
   * @param token:   to validate request
   * @param voterId: voter id
   * @return : return the voter response if voter is present else return null
   */
  public VoterResponseDTO isVoterRegisteredInConstituency(@CookieValue("token") String token, long voterId) {
    Optional<Voter> voterOptional = voterRepository.findById(voterId);
    return voterOptional.map(voter -> {
      UserResponseDTO userResponseDTO = userController.getUser(token, voterId);
      return ModelToResponse.parseVoterToVoterResponse(voterOptional.get(), userResponseDTO);
    }).orElse(null);
  }

  /**
   * count all voter in constituency
   *
   * @param constituencyId: id of constituency
   * @return : count of voter
   */
  public long countAllVoterInConstituency(long constituencyId) {
    return voterRepository.countAllByConstituencyId(constituencyId);
  }

  /**
   * get the constituency of voter
   *
   * @param voterId: if of voter
   * @return :return the voter response if found else return null
   */
  public VoterResponseDTO getVoterConstituency(long voterId) {
    Optional<Voter> optionalVoter = voterRepository.findById(voterId);
    return optionalVoter.map(ModelToResponse::parseVoterToVoterResponse).orElse(null);
  }
}
