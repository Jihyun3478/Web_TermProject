package web.termproject.service;

import org.apache.coyote.BadRequestException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.domain.dto.response.ClubResponseDTO;

import java.net.MalformedURLException;
import java.util.List;

public interface ApplyMemberService {
    public ResponseEntity<String> uploadFiles(MultipartFile file, Long clubId, String loginId) throws BadRequestException;

    ResponseEntity<Resource> downloadFile() throws MalformedURLException;
    ResponseEntity<Resource> downloadApplyForm(Long clubId, Long memberId) throws MalformedURLException;

    List<ClubResponseDTO> getClubList(String loginId) throws BadRequestException;
}
