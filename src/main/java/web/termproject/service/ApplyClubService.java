package web.termproject.service;

import web.termproject.domain.dto.request.ApplyClubRequestDTO;
import web.termproject.domain.entity.ApplyClub;

import java.util.List;
import java.util.Optional;

public interface ApplyClubService {
    ApplyClub createApplyClub(ApplyClubRequestDTO requestDTO);
    List<ApplyClub> findAll();
    ApplyClub findById(Long id);
}
