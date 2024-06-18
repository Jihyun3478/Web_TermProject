package web.termproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.termproject.domain.dto.response.ApplyMemberReponseDTO;
import web.termproject.domain.dto.response.ClubResponseDTO;
import web.termproject.domain.dto.response.MemberResponseDTO;
import web.termproject.domain.dto.response.ProfessorResponseDTO;
import web.termproject.domain.entity.ApplyMember;
import web.termproject.domain.entity.Club;
import web.termproject.domain.entity.Member;
import web.termproject.domain.entity.Professor;
import web.termproject.repository.ClubRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    @Transactional(readOnly = true)
    public List<ClubResponseDTO> findAll() {
        List<Club> clubList = clubRepository.findAll();
        List<ClubResponseDTO> responseDTOS = new ArrayList<>();
        for (Club club : clubList) {
            ClubResponseDTO responseDTO = ClubResponseDTO.builder()
                    .id(club.getId())
                    .applyClubId(club.getApplyClub().getId())
                    .clubType(club.getClubType())
                    .name(club.getName())
                    .introduce(club.getIntroduce())
                    .history(club.getHistory())
                    .imageRoute(club.getImageRoute())
                    .meetingTime(String.valueOf(club.getMeetingTime()))
                    .president(club.getPresident())
                    .vicePresident(club.getVicePresident())
                    .generalAffairs(club.getGeneralAffairs())
                    .professor(convertToProfessorResponseDTO(club.getApplyClub().getProfessor()))
                    .MasterMember(convertToMemberResponseDTO(club.getApplyClub().getMember()))
                    .applyMember(club.getApplyMemberList().stream()
                            .map(this::convertToApplyMemberResponseDTO)
                            .collect(Collectors.toList()))
                    .build();

            responseDTOS.add(responseDTO);
        }
        return responseDTOS;
    }

    private ProfessorResponseDTO convertToProfessorResponseDTO(Professor professor) {
        if (professor == null) {
            return null;
        }
        return ProfessorResponseDTO.builder()
                .id(professor.getId())
                .name(professor.getName())
                .major(professor.getMajor())
                .phoneNum(professor.getPhoneNum())
                .email(professor.getEmail())
                .build();
    }

    private MemberResponseDTO convertToMemberResponseDTO(Member member) {
        if (member == null) {
            return null;
        }
        return MemberResponseDTO.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .loginPw(member.getLoginPw())
                .name(member.getName())
                .stuNum(member.getStuNum())
                .major(member.getMajor())
                .phoneNum(member.getPhoneNum())
                .email(member.getEmail())
                .gender(member.getGender())
                .birthDate(member.getBirthDate())
                .role(member.getRole())
                .build();
    }

    public ApplyMemberReponseDTO convertToApplyMemberResponseDTO(ApplyMember applyMember) {
        Member member = applyMember.getMember();
        MemberResponseDTO memberResponseDTO = MemberResponseDTO.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .loginPw(member.getLoginPw())
                .name(member.getName())
                .stuNum(member.getStuNum())
                .major(member.getMajor())
                .phoneNum(member.getPhoneNum())
                .email(member.getEmail())
                .gender(member.getGender())
                .birthDate(member.getBirthDate())
                .role(member.getRole())
                .build();

        return ApplyMemberReponseDTO.builder()
                .id(applyMember.getId())
                .applyMemberStatus(applyMember.getApplyMemberStatus())
                .member(memberResponseDTO)
                .build();
    }
}
