package web.termproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.termproject.domain.dto.request.ApplyClubRequestDTO;
import web.termproject.exception.ApiResponse;
import web.termproject.domain.dto.response.ApplyClubResponseDTO;
import web.termproject.exception.ResponseCode;
import web.termproject.service.ApplyClubService;

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
        ApplyClubResponseDTO responseDTO = applyClubService.createApplyClub(requestDTO);
        return ResponseEntity.ok(ApiResponse.response(ResponseCode.Created, "동아리 등록 신청 완료", responseDTO));
    }
}
