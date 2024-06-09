package web.termproject.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.termproject.domain.dto.request.RefuseApplyClubDTO;
import web.termproject.domain.dto.response.*;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.domain.entity.Club;
import web.termproject.domain.status.ApplyClubStatus;
import web.termproject.domain.status.ClubType;
import web.termproject.exception.ResponseCode;
import web.termproject.service.ApplyClubService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final ApplyClubService applyClubService;
    private final ModelMapper modelMapper;

    @GetMapping("/list")
    public ResponseEntity<?> applyClubList() {
        List<ApplyClub> applyClubList = applyClubService.findAll();
        List<ApplyClubResponseDTO> responseDTOS = new ArrayList<>();

        for (ApplyClub applyClub : applyClubList) {
            ApplyClubResponseDTO responseDTO = ApplyClubResponseDTO.builder()
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
            responseDTOS.add(responseDTO);
        }
        return ResponseEntity.ok(ApiResponse.response(ResponseCode.OK, responseDTOS));
    }

    @PostMapping("/accept/{applyClubId}")
    public ResponseEntity<?> acceptClub(@PathVariable("applyClubId") Long applyClubId) {
        ApplyClub applyClub = applyClubService.findById(applyClubId);
        Club club = applyClubService.createClub(applyClub);

        ClubResponseDTO responseDTO = ClubResponseDTO.builder()
                .clubType(club.getClubType())
                .name(club.getName())
                .build();
        responseDTO.setProfessor(modelMapper.map(applyClub.getProfessor(), ProfessorResponseDTO.class));
        responseDTO.setMasterMember(modelMapper.map(applyClub.getMember(), MemberResponseDTO.class));
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
