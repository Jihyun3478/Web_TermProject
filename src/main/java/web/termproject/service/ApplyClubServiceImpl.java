package web.termproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.termproject.domain.dto.request.ApplyClubRequestDTO;
import web.termproject.domain.dto.request.ClubRequestDTO;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.domain.entity.Club;
import web.termproject.domain.entity.Member;
import web.termproject.domain.entity.Professor;
import web.termproject.exception.CustomIllegalArgumentException;
import web.termproject.exception.ErrorCode;
import web.termproject.repository.ApplyClubRepository;
import web.termproject.repository.ClubRepository;
import web.termproject.repository.MemberRepository;
import web.termproject.repository.ProfessorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplyClubServiceImpl implements ApplyClubService {

    private final MemberRepository memberRepository;
    private final ProfessorRepository professorRepository;
    private final ApplyClubRepository applyClubRepository;
    private final ClubRepository clubRepository;

    @Override
    public ApplyClub createApplyClub(ApplyClubRequestDTO requestDTO) {
        Member member = memberRepository.findByLoginId(requestDTO.getLoginId());
        Professor professor = professorRepository.findByLoginId(requestDTO.getProfessorLoginId());
        if(member == null || professor == null) {
            throw new CustomIllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND, "존재하지 않는 사용자입니다.");
        }
        ApplyClub saveApplyClub = ApplyClub.createApplyClub(requestDTO.getClubType(), requestDTO.getClubName(), member, professor);

        return applyClubRepository.save(saveApplyClub);
    }

    @Override
    public Club createClub(ApplyClub applyClub) {
        Club club = new Club();
        Club savedClub = club.createClub(applyClub);

        return clubRepository.save(savedClub);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ApplyClub> findAll() {
        return applyClubRepository.findAll();
    }

    @Override
    public ApplyClub findById(Long id) {
        return applyClubRepository.findById(id).orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.APPLY_CLUB_NOT_FOUND, "동아리 신청내역이 존재하지 않습니다."));
    }
}
