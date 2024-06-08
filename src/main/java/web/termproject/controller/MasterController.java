package web.termproject.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.domain.dto.request.ClubRequestDTO;
import web.termproject.domain.dto.response.ApplyMemberReponseDTO;
import web.termproject.domain.dto.response.ClubResponseDTO;
import web.termproject.domain.status.ApplyMemberStatus;
import web.termproject.security.util.SecurityUtil;
import web.termproject.service.ApplyMemberService;
import web.termproject.service.ImageService;
import web.termproject.service.MasterService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

/**
 * 마스터 관리자 Controller
 *
 * 동아리 승인 이후의 시나리오로 작성함
 */
@RestController
@RequestMapping("/master/club")
@RequiredArgsConstructor
public class MasterController {
    private final MasterService masterService;
    private final ImageService imageService;
    private final ApplyMemberService applyMemberService;

    @GetMapping("/list")
    public List<ClubResponseDTO> clubs() throws BadRequestException {
        String loginId = SecurityUtil.getLoginId();
        return masterService.getMasterClubsInfo(loginId);
    }

    @PutMapping("/{clubId}")
    public ClubResponseDTO updateMasterClubInfo(@PathVariable("clubId") Long clubId, @RequestBody ClubRequestDTO clubRequestDTO) throws BadRequestException {
        String loginId = SecurityUtil.getLoginId();
        return masterService.updateMasterClubInfo(clubId, loginId, clubRequestDTO);
    }

    @PostMapping(value = "/uploadImage/{clubId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> uploadClubImage(@PathVariable("clubId") Long clubId, @RequestParam("image") MultipartFile image) throws IOException {
        Map<String, String> imgUrl = imageService.uploadImage(image, "club/" + clubId);
        masterService.updateMasterClubImgUrl(clubId, imgUrl.get("url"));
        return imgUrl;
    }

    @PostMapping("/applyMember/{applyMemberId}")
    public ResponseEntity<String> updateApplyMemberStatus(@PathVariable("applyMemberId") Long applyMemberId, @RequestParam("ApplyMemberStatus") ApplyMemberStatus applyMemberStatus) {
        return masterService.updateApplyMemberStatus(applyMemberId, applyMemberStatus);
    }

    @GetMapping("/applyMember/{clubId}/{memberId}")
    public ResponseEntity<Resource> downloadApplyForm(@PathVariable("clubId") Long clubId, @PathVariable("memberId")Long memberId) throws MalformedURLException {
        return applyMemberService.downloadApplyForm(clubId, memberId);
    }
}
