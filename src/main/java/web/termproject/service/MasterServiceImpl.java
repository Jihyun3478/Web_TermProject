package web.termproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import web.termproject.domain.dto.request.ClubRequestDTO;
import web.termproject.domain.dto.response.*;
import web.termproject.domain.entity.ApplyMember;
import web.termproject.domain.entity.Club;
import web.termproject.domain.entity.Member;
import web.termproject.domain.status.ApplyMemberStatus;
import web.termproject.exception.ErrorCode;
import web.termproject.repository.ApplyMemberRepository;
import web.termproject.repository.MasterRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MasterServiceImpl implements MasterService {
    private final MasterRepository masterRepository;
    private final ModelMapper modelMapper;
    private final ApplyMemberRepository applyMemberRepository;
    private final MemberService memberService;

    @Override
    public List<ClubResponseDTO> getMasterClubsInfo(String loginId) throws BadRequestException {
        Member findMember = memberService.findByLoginId(loginId);
        if(!masterRepository.existsById(findMember.getId())) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return masterRepository.findClubsByMemberId(findMember.getId()).stream()
                .map(this::getClubResponseDTO)
                .collect(Collectors.toList());
    }

    private ClubResponseDTO getClubResponseDTO(Club club) {
        ClubResponseDTO clubResponseDTO = modelMapper.map(club, ClubResponseDTO.class);
        List<ApplyMemberReponseDTO> applyMemberReponseDTOList = masterRepository.findApplyMemberByClubId(club.getId()).stream()
                .map(applyMember -> {
                    ApplyMemberReponseDTO applyMemberReponseDTO = modelMapper.map(applyMember, ApplyMemberReponseDTO.class);
                    applyMemberReponseDTO.setMember(modelMapper.map(applyMember.getMember(), MemberResponseDTO.class));
                    return applyMemberReponseDTO;
                })
                .toList();
        clubResponseDTO.setProfessor(modelMapper.map(club.getApplyClub().getProfessor(), ProfessorResponseDTO.class));
        clubResponseDTO.setMasterMember(modelMapper.map(club.getApplyClub().getMember(), MemberResponseDTO.class));
        clubResponseDTO.setApplyMember(applyMemberReponseDTOList);
        return clubResponseDTO;
    }


    @Override
    public ClubResponseDTO updateMasterClubInfo(Long clubId, String loginId, ClubRequestDTO clubRequestDTO) throws BadRequestException {
        Member member = memberService.findByLoginId(loginId);
        Optional<Club> optionalClub = masterRepository.findClubByClubIdAndMemberId(clubId, member.getId());
        if(optionalClub.isPresent()) {
            Club club = optionalClub.get();
            modelMapper.map(clubRequestDTO, club);
            masterRepository.save(club);
            return getClubResponseDTO(club);
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_ENTITY.getMessage());
    }

    @Override
    public ResponseEntity<String> updateApplyMemberStatus(Long applyMemberId, ApplyMemberStatus applyMemberStatus) {
        Optional<ApplyMember> optionalApplyMember = masterRepository.findApplyMemberByApplyMemberId(applyMemberId);
        if(optionalApplyMember.isPresent()) {
            ApplyMember applyMember = optionalApplyMember.get();
            applyMember.setApplyMemberStatus(applyMemberStatus);
            applyMemberRepository.save(applyMember);
            return ResponseEntity.ok("updateApplyMemberStatus");
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_ENTITY.getMessage());
    }

    @Override
    public void updateMasterClubImgUrl(Long clubId, String imageRoute) {
        Optional<Club> optionalClub = masterRepository.findById(clubId);
        if(optionalClub.isPresent()) {
             Club club = optionalClub.get();
             club.updateImageRouteInfo(imageRoute);
             masterRepository.save(club);
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_ENTITY.getMessage());
    }
}
