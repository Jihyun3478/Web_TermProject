package web.termproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import web.termproject.domain.dto.request.RefuseApplyClubDTO;
import web.termproject.domain.dto.response.ApplyClubResponseDTO;
import web.termproject.domain.dto.response.ClubResponseDTO;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.domain.entity.Member;
import web.termproject.domain.status.ApplyClubStatus;
import web.termproject.domain.status.RoleType;
import web.termproject.exception.ApiResponse;
import web.termproject.exception.ResponseCode;
import web.termproject.service.ApplyClubService;
import web.termproject.service.MemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final ApplyClubService applyClubService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<?> applyClubList() {
        List<ApplyClubResponseDTO> responseDTOS = applyClubService.findAll();
        return ResponseEntity.ok(ApiResponse.response(ResponseCode.OK, "관리자 동아리 신청 목록 조회", responseDTOS));
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @GetMapping("/list/{applyClubId}")
    public ResponseEntity<?> applyClubDetail(@PathVariable("applyClubId") Long applyClubId) {
        ApplyClub applyClub = applyClubService.findById(applyClubId);
        ApplyClubResponseDTO responseDTO = ApplyClubResponseDTO.builder()
                .applyClubId(applyClub.getId())
                .applyClubStatus(applyClub.getApplyClubStatus())
                .clubType(applyClub.getClubType())
                .clubName(applyClub.getClubName())
                .name(applyClub.getMember().getName())
                .major(applyClub.getMember().getMajor())
                .stuNum(applyClub.getMember().getStuNum())
                .phoneNum(applyClub.getMember().getPhoneNum())
                .pName(applyClub.getProfessor().getName())
                .pMajor(applyClub.getProfessor().getMajor())
                .pPhoneNum(applyClub.getProfessor().getPhoneNum())
                .build();

        return ResponseEntity.ok(ApiResponse.response(ResponseCode.OK, "관리자 동아리 신청 상세 조회", responseDTO));
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @PostMapping("/accept/{applyClubId}")
    public ResponseEntity<?> acceptClub(@PathVariable("applyClubId") Long applyClubId) {
        ApplyClub applyClub = applyClubService.findById(applyClubId);
        applyClub.setApplyClubStatus(ApplyClubStatus.ACCEPT);
        applyClubService.save(applyClub);

        ClubResponseDTO responseDTO = applyClubService.createClub(applyClub);

        Member member = applyClub.getMember();
        member.setRole(RoleType.MASTER_MEMBER);
        memberService.save(member);

        return ResponseEntity.ok(ApiResponse.response(ResponseCode.OK, "관리자 동아리 신청 승인", responseDTO));
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @PostMapping("/refuse/{applyClubId}")
    public ResponseEntity<?> refuseClub(@PathVariable("applyClubId") Long applyClubId, @RequestBody RefuseApplyClubDTO refuseApplyClubDTO) {
        ApplyClubResponseDTO responseDTO = applyClubService.refuseClub(applyClubId, refuseApplyClubDTO.getRefuseReason());
        return ResponseEntity.ok(ApiResponse.response(ResponseCode.OK, "관리자 동아리 신청 거절", responseDTO));
    }
}
