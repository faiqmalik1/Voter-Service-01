package com.votingsystem.voterservice.repository;

import com.votingsystem.voterservice.model.Voter;
import com.votingsystem.voterservice.model.Votes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotesRepository extends JpaRepository<Votes, Long> {

  Optional<Votes> findByVoterIdAndPoolingId(Voter voterId, long poolingId);

  long countVotesByCandidateIdAndPoolingId(long candidateId, long poolingId);
}
