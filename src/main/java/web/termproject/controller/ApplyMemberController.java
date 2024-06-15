package web.termproject.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.domain.dto.response.ClubResponseDTO;
import web.termproject.security.util.SecurityUtil;
import web.termproject.service.ApplyMemberService;

import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/applyMember")
@RequiredArgsConstructor
public class ApplyMemberController {
    private final ApplyMemberService applyMemberService;

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile() throws MalformedURLException {
        return applyMemberService.downloadFile();
    }

    @PostMapping(value = "/upload/{clubId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFiles(@RequestParam("files") MultipartFile file, @PathVariable("clubId") Long clubId) throws BadRequestException {
        String loginId = SecurityUtil.getLoginId();
        return applyMemberService.uploadFiles(file, clubId, loginId);
    }

    @GetMapping("/clubList")
    public List<ClubResponseDTO> getNotApplyMemberClubList() throws BadRequestException {
        String loginId = SecurityUtil.getLoginId();
        return applyMemberService.getNotApplyMemberClubList(loginId);
    }
}
