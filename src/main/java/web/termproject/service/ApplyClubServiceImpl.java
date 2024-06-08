package web.termproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import web.termproject.domain.dto.request.ApplyClubRequestDTO;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.domain.entity.Member;
import web.termproject.domain.entity.Professor;
import web.termproject.exception.CustomIllegalArgumentException;
import web.termproject.exception.ErrorCode;
import web.termproject.repository.ApplyClubRepository;
import web.termproject.repository.MemberRepository;
import web.termproject.repository.ProfessorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplyClubServiceImpl implements ApplyClubService {

    private final MemberRepository memberRepository;
    private final ProfessorRepository professorRepository;
    private final ApplyClubRepository applyClubRepository;

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
    public List<ApplyClub> findAll() {
        return applyClubRepository.findAll();
    }
}
