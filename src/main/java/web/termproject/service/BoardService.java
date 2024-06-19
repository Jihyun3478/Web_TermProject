package web.termproject.service;

import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.domain.dto.request.board.ActivityPhotoRequestDTO;
import web.termproject.domain.dto.request.board.ActivityVideoRequestDTO;
import web.termproject.domain.dto.request.board.NoticeClubRequestDTO;
import web.termproject.domain.dto.request.board.RecruitMemberRequestDTO;
import web.termproject.domain.dto.response.board.ActivityPhotoResponseDTO;
import web.termproject.domain.dto.response.board.ActivityVideoResponseDTO;
import web.termproject.domain.dto.response.board.NoticeClubResponseDTO;
import web.termproject.domain.dto.response.board.RecruitMemberResponseDTO;
import web.termproject.domain.entity.Board;
import web.termproject.domain.entity.Club;

import java.net.MalformedURLException;
import java.util.List;

public interface BoardService {

    //부원 모집 등록 -> 모든 게시글에 이미지 및 동영상 등록 가능
    Boolean saveRecruitMember(RecruitMemberRequestDTO boardRequestDTO, MultipartFile image, String loginId);

    //활동 사진 등록
    Boolean saveActivityPhoto(ActivityPhotoRequestDTO boardRequestDTO, MultipartFile image, String loginId);

    //활동 영상 등록
    Boolean saveActivityVideo(ActivityVideoRequestDTO boardRequestDTO, String loginId);

    //동아리 공지 게시글 전체 조회
    List<NoticeClubResponseDTO> findAllAnnouncement(List<Long> clubIds);


    // 동아리 공지 특정 게시글 조회
    NoticeClubResponseDTO getAnnouncement(Long boardId);

    List<NoticeClubResponseDTO> findAllAnnouncementForMasterMember(Long memberId);

    List<NoticeClubResponseDTO> findClubAnnouncements(Long memberId);

    //부원 모집 게시글 전체조회
    List<RecruitMemberResponseDTO> findAllRecruitMember();

    //부원 모집 특정 게시글 조회
    RecruitMemberResponseDTO getRecruitMember(Long boardId);

    //활동 사진 게시글 전체 조회
    List<ActivityPhotoResponseDTO> findAllActivityPhoto();

    //활동 사진 특정 게시글 조회
    ActivityPhotoResponseDTO getActivityPhoto(Long boardId);

    //활동 영상 게시글 전체 조회
    List<ActivityVideoResponseDTO> findAllActivityVideo();

    //활동 영상 특정 게시글 조회
    ActivityVideoResponseDTO getActivityVideo(Long boardId);


    Boolean saveNoticeClub(NoticeClubRequestDTO boardRequestDTO, MultipartFile image, String loginId);

    Resource getImage(String imageRoute)throws MalformedURLException;

    Resource loadAsResource(String imagePath);

    List<NoticeClubResponseDTO> findAllAnnouncementIncludingPublic(List<Long> clubIds);
}
