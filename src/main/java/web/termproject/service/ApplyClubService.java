package web.termproject.service;

import web.termproject.domain.dto.request.ApplyClubRequestDTO;
import web.termproject.domain.dto.request.ClubRequestDTO;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.domain.entity.Club;

import java.util.List;
import java.util.Optional;

public interface ApplyClubService {
    ApplyClub createApplyClub(ApplyClubRequestDTO requestDTO);
    Club createClub(ApplyClub applyClub);
    List<ApplyClub> findAll();
    ApplyClub findById(Long id);
}
