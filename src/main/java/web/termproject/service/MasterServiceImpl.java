package web.termproject.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import web.termproject.domain.dto.request.ClubRequestDTO;
import web.termproject.domain.dto.response.ClubResponseDTO;
import web.termproject.domain.dto.response.ErrorCode;
import web.termproject.domain.entity.Club;
import web.termproject.repository.MasterRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {
    private final MasterRepository masterRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ClubResponseDTO> getMasterClubsInfo(Long memberId) {
        return masterRepository.findClubsByMemberId(memberId).stream()
                .map(club -> modelMapper.map(club, ClubResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ClubResponseDTO findMasterClubInfo(Long clubId) {
        return modelMapper.map(masterRepository.findById(clubId), ClubResponseDTO.class);
    }

    @Override
    public ClubResponseDTO updateMasterClubInfo(Long clubId, ClubRequestDTO clubRequestDTO) {
        Optional<Club> optionalClub = masterRepository.findById(clubId);
        if(optionalClub.isPresent()) {
            Club club = optionalClub.get();
            modelMapper.map(clubRequestDTO, club);
            masterRepository.save(club);
            return modelMapper.map(club, ClubResponseDTO.class);
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_ENTITY.getMessage());
    }
}
