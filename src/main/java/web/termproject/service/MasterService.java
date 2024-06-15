package web.termproject.service;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.domain.dto.request.ClubRequestDTO;
import web.termproject.domain.dto.response.ApplyMemberReponseDTO;
import web.termproject.domain.dto.response.ClubResponseDTO;
import web.termproject.domain.status.ApplyMemberStatus;

import java.util.List;

public interface MasterService {
    List<ClubResponseDTO> getMasterClubsInfo(String loginId) throws BadRequestException;
    ClubResponseDTO updateMasterClubInfo(Long clubId, String memberId, ClubRequestDTO clubRequestDTO) throws BadRequestException;
    ResponseEntity<String> updateApplyMemberStatus(Long applyMemberId, ApplyMemberStatus applyMemberStatus);

    void updateMasterClubImgUrl(Long clubId, String imgUrl);
}
