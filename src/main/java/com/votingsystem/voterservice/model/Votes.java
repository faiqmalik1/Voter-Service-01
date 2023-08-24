package com.votingsystem.voterservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Votes {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long votesId;
  @ManyToOne
  @JoinColumn(name = "voter_id")
  private Voter voterId;
  private long candidateId;
  private long poolingId;

}