package web.termproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.termproject.domain.dto.request.ApplyClubRequestDTO;
import web.termproject.domain.dto.response.ApiResponse;
import web.termproject.domain.dto.response.ApplyClubResponseDTO;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.exception.ResponseCode;
import web.termproject.service.ApplyClubService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/applyClub")
public class ApplyClubController {

    private final ApplyClubService applyClubService;

    /**
     * 동아리 등록 신청
     * @param requestDTO
     */
    @PostMapping("/create")
    public ResponseEntity<?> createApplyClub(@RequestBody ApplyClubRequestDTO requestDTO) {
        ApplyClub applyClub = applyClubService.createApplyClub(requestDTO);
        ApplyClubResponseDTO responseDTO = ApplyClubResponseDTO.builder()
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

    /**
     * 동아리 신청 목록
     */
    @GetMapping("/list")
    public ResponseEntity<?> applyClubList() {
        List<ApplyClub> applyClubList = applyClubService.findAll();
        return ResponseEntity.ok(ApiResponse.response(ResponseCode.OK, applyClubList));
    }
}
