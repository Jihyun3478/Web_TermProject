package web.termproject.service;

import web.termproject.domain.dto.request.PSignupRequestDTO;
import web.termproject.domain.entity.Professor;

public interface ProfessorService {
    void createProfessor(PSignupRequestDTO psignupRequestDTO);
    Professor findByName(String professorName);
}
