package web.termproject.service;

import web.termproject.domain.dto.request.SignupRequestDTO;
import web.termproject.domain.dto.response.MemberResponseDTO;

public interface AdminService {
    MemberResponseDTO createAdmin(SignupRequestDTO signupRequestDTO);
}
