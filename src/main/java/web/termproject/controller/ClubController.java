package web.termproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.termproject.domain.dto.response.ClubResponseDTO;
import web.termproject.exception.ApiResponse;
import web.termproject.exception.ResponseCode;
import web.termproject.service.ClubService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/club")
public class ClubController {

    private final ClubService clubService;

    @GetMapping("/list")
    public ResponseEntity<?> applyClubList() {
        List<ClubResponseDTO> responseDTOS = clubService.findAll();
        return ResponseEntity.ok(ApiResponse.response(ResponseCode.OK, "전체 동아리 목록 조회", responseDTOS));
    }
}
