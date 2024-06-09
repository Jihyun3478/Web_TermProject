package web.termproject.service;

import org.modelmapper.ModelMapper;
import web.termproject.domain.dto.request.ApplyClubRequestDTO;
import web.termproject.domain.dto.response.ApplyClubResponseDTO;
import web.termproject.domain.dto.response.ClubResponseDTO;
import web.termproject.domain.entity.ApplyClub;

import java.util.List;

public interface ApplyClubService {
    ApplyClubResponseDTO createApplyClub(ApplyClubRequestDTO requestDTO);
    ClubResponseDTO createClub(ApplyClub applyClub);
    ApplyClubResponseDTO refuseClub(Long applyClubId, String refuseReason);
    List<ApplyClubResponseDTO> findAll();
    ApplyClub findById(Long id);
    ModelMapper getModelMapper();
}
