package web.termproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.termproject.domain.dto.request.RefuseApplyClubDTO;
import web.termproject.domain.dto.response.ApiResponse;
import web.termproject.domain.dto.response.ApplyClubResponseDTO;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.domain.status.ApplyClubStatus;
import web.termproject.exception.ResponseCode;
import web.termproject.service.ApplyClubService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final ApplyClubService applyClubService;

    @PostMapping("/accept/{applyClubId}")
    public ResponseEntity<?> acceptClub(@PathVariable("applyClubId") Long applyClubId) {
        ApplyClub applyClub = applyClubService.findById(applyClubId);
        ApplyClubResponseDTO responseDTO = ApplyClubResponseDTO.builder()
                .applyClubStatus(ApplyClubStatus.ACCEPT)
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
        return ResponseEntity.ok(ApiResponse.response(ResponseCode.OK, responseDTO));
    }

    @PostMapping("/refuse/{applyClubId}")
    public ResponseEntity<?> refuseClub(@PathVariable("applyClubId") Long applyClubId, @RequestBody RefuseApplyClubDTO refuseApplyClubDTO) {
        ApplyClub applyClub = applyClubService.findById(applyClubId);
        ApplyClubResponseDTO responseDTO = ApplyClubResponseDTO.builder()
                .refuseReason(refuseApplyClubDTO.getRefuseReason())
                .applyClubStatus(ApplyClubStatus.REFUSE)
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
        return ResponseEntity.ok(ApiResponse.response(ResponseCode.OK, responseDTO));
    }
}
