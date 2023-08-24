package com.votingsystem.voterservice.resources;

import com.votingsystem.voterservice.model.Voter;
import resources.user.UserResponseDTO;
import resources.voter.VoterResponseDTO;

public class ModelToResponse {

  public static VoterResponseDTO parseVoterToVoterResponse(Voter voter, UserResponseDTO userResponseDTO) {
    VoterResponseDTO voterResponseDTO = new VoterResponseDTO();
    voterResponseDTO.setVoterId(voter.getVoterId());
    voterResponseDTO.setVoterName(userResponseDTO.getName());
    voterResponseDTO.setCnic(userResponseDTO.getCNIC());
    voterResponseDTO.setConstitutionId(voter.getConstituencyId());
    return voterResponseDTO;
  }

  public static VoterResponseDTO parseVoterToVoterResponse(Voter voter) {
    VoterResponseDTO voterResponseDTO = new VoterResponseDTO();
    voterResponseDTO.setVoterId(voter.getVoterId());
    voterResponseDTO.setConstitutionId(voter.getConstituencyId());
    return voterResponseDTO;
  }
}