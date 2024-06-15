package web.termproject.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import web.termproject.domain.dto.request.PSignupRequestDTO;
import web.termproject.domain.entity.Professor;
import web.termproject.repository.ProfessorRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProfessorServiceTest {

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private ProfessorRepository professorRepository;

    @Test
    void 교수_생성() {
        PSignupRequestDTO psignupRequestDTO = PSignupRequestDTO.builder()
                .pLoginId("prof1234")
                .loginPw("password")
                .name("김교수")
                .major("컴퓨터소프트웨어공학과")
                .phoneNum("010-1234-5678")
                .email("prof@example.com")
                .build();

        professorService.createProfessor(psignupRequestDTO);

        Professor professor = professorRepository.findByLoginId("prof1234");
        assertThat(professor).isNotNull();
        assertThat(professor.getLoginId()).isEqualTo("prof1234");
        assertThat(professor.getName()).isEqualTo("김교수");
        assertThat(professor.getMajor()).isEqualTo("컴퓨터소프트웨어공학과");
        assertThat(professor.getPhoneNum()).isEqualTo("010-1234-5678");
        assertThat(professor.getEmail()).isEqualTo("prof@example.com");
    }
}
