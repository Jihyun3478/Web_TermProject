package web.termproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import web.termproject.domain.dto.request.ApplyClubRequestDTO;
import web.termproject.domain.dto.response.ApplyClubResponseDTO;
import web.termproject.domain.dto.response.ClubResponseDTO;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.domain.entity.Member;
import web.termproject.domain.entity.Professor;
import web.termproject.domain.status.ApplyClubStatus;
import web.termproject.domain.status.ClubType;
import web.termproject.repository.ApplyClubRepository;
import web.termproject.repository.ProfessorRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ApplyClubServiceTest {

    @Autowired
    private ApplyClubService applyClubService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ApplyClubRepository applyClubRepository;

    private Member testMember;
    private Professor testProfessor;

    @BeforeEach
    void initData() {
        testMember = Member.builder()
                .id(1L)
                .loginId("test1234")
                .loginPw("test1234")
                .name("홍길동")
                .stuNum("2024")
                .major("컴퓨터소프트웨어공학과")
                .phoneNum("000-0000-0000")
                .email("홍길동@kumoh.ac.kr")
                .gender("남")
                .birthDate("2000-01-01")
                .build();

        testProfessor = Professor.builder()
                .id(1L)
                .loginId("pTest1234")
                .loginPw("pTest1234")
                .name("교수님1")
                .major("컴퓨터소프트웨어공학과")
                .phoneNum("222-2222-2222")
                .email("교수님@kumoh.ac.kr")
                .build();
    }

    @Test
    void createApplyClub() {
        ApplyClubRequestDTO requestDTO = ApplyClubRequestDTO.builder()
                .clubType(ClubType.CENTRAL)
                .clubName("Basketball Club")
                .name(testMember.getName())
                .major(testMember.getMajor())
                .stuNum(testMember.getStuNum())
                .phoneNum(testMember.getPhoneNum())
                .pName(testProfessor.getName())
                .pMajor(testProfessor.getMajor())
                .pPhoneNum(testProfessor.getPhoneNum())
                .build();

        // When
        ApplyClubResponseDTO responseDTO = applyClubService.createApplyClub(requestDTO);

        // Then
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getClubName()).isEqualTo("Basketball Club");
        assertThat(responseDTO.getClubType()).isEqualTo(ClubType.CENTRAL);
    }

    @Test
    void createClub() {
        // Given
        Member member = memberService.findByLoginId("test1234");
        Professor professor = professorRepository.findByLoginId("pTest1234");

        ApplyClub applyClub = ApplyClub.createApplyClub(ClubType.CENTRAL, "Basketball Club", member, professor);
        applyClubRepository.save(applyClub);

        // When
        ClubResponseDTO responseDTO = applyClubService.createClub(applyClub);

        // Then
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getName()).isEqualTo("Basketball Club");
    }

    @Test
    void refuseClub() {
        // Given
        Member member = memberService.findByLoginId("test1234");
        Professor professor = professorRepository.findByLoginId("pTest1234");

        ApplyClub applyClub = ApplyClub.createApplyClub(ClubType.CENTRAL, "Basketball Club", member, professor);
        applyClubRepository.save(applyClub);

        // When
        ApplyClubResponseDTO responseDTO = applyClubService.refuseClub(applyClub.getId(), "Not interested");

        // Then
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getApplyClubStatus()).isEqualTo(ApplyClubStatus.REFUSE);
        assertThat(responseDTO.getRefuseReason()).isEqualTo("Not interested");
    }

    @Test
    void findAll() {
        // Given
        List<ApplyClubResponseDTO> initialList = applyClubService.findAll();
        assertThat(initialList).isEmpty();

        Member member = memberService.findByLoginId("test1234");
        Professor professor = professorRepository.findByLoginId("pTest1234");

        ApplyClub applyClub = ApplyClub.createApplyClub(ClubType.CENTRAL, "Basketball Club", member, professor);
        applyClubRepository.save(applyClub);

        // When
        List<ApplyClubResponseDTO> responseDTOS = applyClubService.findAll();

        // Then
        assertThat(responseDTOS).isNotEmpty();
    }

    @Test
    void findById() {
        // Given
        Member member = memberService.findByLoginId("test1234");
        Professor professor = professorRepository.findByLoginId("pTest1234");

        ApplyClub applyClub = ApplyClub.createApplyClub(ClubType.CENTRAL, "Basketball Club", member, professor);
        applyClubRepository.save(applyClub);

        // When
        ApplyClub result = applyClubService.findById(applyClub.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getClubName()).isEqualTo("Basketball Club");
    }

    @Test
    void findById_throwsIllegalArgumentException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> applyClubService.findById(999L));
    }
}
