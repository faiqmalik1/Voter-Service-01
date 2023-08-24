package com.votingsystem.voterservice.repository;

import com.votingsystem.voterservice.model.Voter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long> {

  Page<Voter> findAllByConstituencyId(long constituencyId, Pageable pageable);

  Optional<Voter> findByVoterIdAndConstituencyId(long voterId, long constituencyId);

  long countAllByConstituencyId(long ConstituencyId);
}