package web.termproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import web.termproject.domain.dto.request.PSignupRequestDTO;
import web.termproject.domain.entity.Professor;
import web.termproject.repository.ProfessorRepository;

@Service
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;

    @Override
    public void createProfessor(PSignupRequestDTO psignupRequestDTO) {
        Professor professor = psignupRequestDTO.toEntity();

        professorRepository.save(professor);
    }

    @Override
    public Professor findByName(String professorName) {
        return professorRepository.findByName(professorName);
    }
}
