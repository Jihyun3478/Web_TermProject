package web.termproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import web.termproject.domain.dto.request.RefuseApplyClubDTO;
import web.termproject.exception.ApiResponse;
import web.termproject.domain.dto.response.ApplyClubResponseDTO;
import web.termproject.domain.dto.response.ClubResponseDTO;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.exception.ResponseCode;
import web.termproject.service.ApplyClubService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final ApplyClubService applyClubService;

    /**
     * 동아리 신청 목록 조회
     */
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<?> applyClubList() {
        List<ApplyClubResponseDTO> responseDTOS = applyClubService.findAll();
        return ResponseEntity.ok(ApiResponse.response(ResponseCode.OK, "관리자 동아리 신청 목록 조회", responseDTOS));
    }

    /**
     * 동아리 신청 승인
     * @param applyClubId
     */
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @PostMapping("/accept/{applyClubId}")
    public ResponseEntity<?> acceptClub(@PathVariable("applyClubId") Long applyClubId) {
        ApplyClub applyClub = applyClubService.findById(applyClubId);
        ClubResponseDTO responseDTO = applyClubService.createClub(applyClub);

        return ResponseEntity.ok(ApiResponse.response(ResponseCode.OK, "관리자 동아리 신청 승인", responseDTO));
    }

    /**
     * 동아리 신청 거절
     * @param applyClubId
     * @param refuseApplyClubDTO
     */
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @PostMapping("/refuse/{applyClubId}")
    public ResponseEntity<?> refuseClub(@PathVariable("applyClubId") Long applyClubId, @RequestBody RefuseApplyClubDTO refuseApplyClubDTO) {
        ApplyClubResponseDTO responseDTO = applyClubService.refuseClub(applyClubId, refuseApplyClubDTO.getRefuseReason());
        return ResponseEntity.ok(ApiResponse.response(ResponseCode.OK, "관리자 동아리 신청 거절", responseDTO));
    }
}
