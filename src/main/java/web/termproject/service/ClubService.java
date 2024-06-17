package web.termproject.service;

import web.termproject.domain.dto.response.ClubResponseDTO;

import java.util.List;

public interface ClubService {
    List<ClubResponseDTO> findAll();
}
