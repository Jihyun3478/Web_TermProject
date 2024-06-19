package web.termproject.service;

import web.termproject.domain.dto.request.ApplyClubRequestDTO;
import web.termproject.domain.dto.response.ApplyClubResponseDTO;
import web.termproject.domain.dto.response.ClubResponseDTO;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.domain.status.ApplyClubStatus;

import java.util.List;

public interface ApplyClubService {
    ApplyClubResponseDTO createApplyClub(ApplyClubRequestDTO requestDTO);
    ApplyClub createApplyClubEntity(ApplyClubRequestDTO requestDTO);
    ClubResponseDTO createClub(ApplyClub applyClub);
    ApplyClubResponseDTO refuseClub(Long applyClubId, String refuseReason);
    List<ApplyClubResponseDTO> findAll();
    List<ApplyClubResponseDTO> findApplyClubByMember(String loginId);
    ApplyClub findById(Long id);
    void save(ApplyClub applyClub);
    List<ApplyClub> findByApplyClubStatus(ApplyClubStatus applyClubStatus);
}
