package web.termproject.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import web.termproject.domain.dto.response.ClubResponseDTO;
import web.termproject.exception.ErrorCode;
import web.termproject.domain.dto.response.MemberResponseDTO;
import web.termproject.domain.dto.response.ProfessorResponseDTO;
import web.termproject.domain.entity.ApplyMember;
import web.termproject.domain.entity.Club;
import web.termproject.domain.entity.Member;
import web.termproject.domain.status.ApplyMemberStatus;
import web.termproject.repository.ApplyMemberRepository;
import web.termproject.repository.MasterRepository;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplyMemberServiceImpl implements ApplyMemberService {
    private final ApplyMemberRepository applyMemberRepository;
    private final MasterRepository masterRepository;
    private final MemberService memberService;
    private final ModelMapper modelMapper;
    private static String uploadDirectory = System.getProperty("user.dir") + "/applyMember";

    @Override
    public ResponseEntity<Resource> downloadFile() throws MalformedURLException {
        Path filePath = Paths.get(uploadDirectory).resolve("applyClubForm.hwp").normalize();
        Resource resource = new UrlResource(filePath.toUri());

        // 파일이 존재하는지 확인
        if (!resource.exists()) {
            throw new RuntimeException("File not found " + "applyClubForm.hwp");
        }

        // 다운로드할 파일의 MIME 타입 설정
        String contentType;
        try {
            contentType = Files.probeContentType(filePath);
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }

        // 다운로드할 파일의 응답 헤더 설정
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @Override
    public ResponseEntity<String> uploadFiles(MultipartFile file, Long clubId, String loginId) throws BadRequestException {
        Member findMember = memberService.findByLoginId(loginId);
        try {
            // 업로드할 디렉토리에 파일 저장
            String fileName = "applyClubForm.hwp";
            String directoryPath = uploadDirectory + "/request/club/" + clubId + "/" + findMember.getId();
            File directory = new File(directoryPath);
            if(!directory.exists())
                directory.mkdirs();
            File destFile = new File(directoryPath + "/" + fileName);
            file.transferTo(destFile);

            if(!applyMemberRepository.existsApplyMemberByMemberIdAndClubId(clubId, findMember.getId()))
            {
                ApplyMember applyMember = ApplyMember.builder()
                        .member(findMember)
                        .applyMemberStatus(ApplyMemberStatus.NOT_CLUB_MEMBER)
                        .club(masterRepository.findById(clubId).get())
                        .build();
                applyMemberRepository.save(applyMember);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to upload files.");
        }


        return ResponseEntity.ok("Files uploaded successfully.");
    }

    @Override
    public ResponseEntity<Resource> downloadApplyForm(Long clubId, Long memberId) throws MalformedURLException {
        Path filePath = Paths.get(uploadDirectory + "/request/club/" + clubId + "/" + memberId).resolve("applyClubForm.hwp").normalize();
        Resource resource = new UrlResource(filePath.toUri());

        // 파일이 존재하는지 확인
        if (!resource.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_ENTITY.getMessage());
        }

        // 다운로드할 파일의 MIME 타입 설정
        String contentType;
        try {
            contentType = Files.probeContentType(filePath);
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }

        // 다운로드할 파일의 응답 헤더 설정
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @Override
    public List<ClubResponseDTO> getNotApplyMemberClubList(String loginId) throws BadRequestException {
        Member findMember = memberService.findByLoginId(loginId);

        return applyMemberRepository.findClubByNotMemberClubId(findMember.getId()).stream()
                .map(this::NotApplyMemberClubListMerge)
                .toList();
    }

    private ClubResponseDTO NotApplyMemberClubListMerge(Club club) {
        ClubResponseDTO clubResponseDTO = modelMapper.map(club, ClubResponseDTO.class);
        clubResponseDTO.setProfessor(modelMapper.map(club.getApplyClub().getProfessor(), ProfessorResponseDTO.class));
        clubResponseDTO.setMasterMember(modelMapper.map(club.getApplyClub().getMember(), MemberResponseDTO.class));
        return clubResponseDTO;
    }
}
