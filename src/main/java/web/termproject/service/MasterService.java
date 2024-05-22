package web.termproject.service;

import org.springframework.http.ResponseEntity;
import web.termproject.domain.dto.request.ClubRequestDTO;
import web.termproject.domain.dto.response.ApplyMemberReponseDTO;
import web.termproject.domain.dto.response.ClubResponseDTO;
import web.termproject.domain.status.ApplyMemberStatus;

import java.util.List;

public interface MasterService {
    List<ClubResponseDTO> getMasterClubsInfo(Long memberId);
    ClubResponseDTO findMasterClubInfo(Long clubId, Long memberId);
    ClubResponseDTO updateMasterClubInfo(Long clubId, Long memberId, ClubRequestDTO clubRequestDTO);
    List<ApplyMemberReponseDTO> getApplyMemberList(Long clubId);

    ResponseEntity<String> updateApplyMemberStatus(Long applyMemberId, ApplyMemberStatus applyMemberStatus);
}
