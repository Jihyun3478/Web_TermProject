package web.termproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import web.termproject.domain.dto.request.ClubRequestDTO;
import web.termproject.domain.dto.response.*;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.domain.entity.Club;
import web.termproject.repository.MasterRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {
    private final MasterRepository masterRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ClubResponseDTO> getMasterClubsInfo(Long memberId) {
        if(!masterRepository.existsById(memberId)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return masterRepository.findClubsByMemberId(memberId).stream()
                .map(this::getClubResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClubResponseDTO findMasterClubInfo(Long clubId, Long memberId) {
        Optional<Club> optionalClub = masterRepository.findClubByClubIdAndMemberId(clubId, memberId);
        if(optionalClub.isPresent())
            return getClubResponseDTO(optionalClub.get());
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_ENTITY.getMessage());
    }

    private ClubResponseDTO getClubResponseDTO(Club club) {
        ClubResponseDTO clubResponseDTO = modelMapper.map(club, ClubResponseDTO.class);
        List<ApplyMemberReponseDTO> applyMemberReponseDTOList = masterRepository.findApplyMemberByClubId(club.getId()).stream()
                .map(applyMember -> {
                    ApplyMemberReponseDTO applyMemberReponseDTO = modelMapper.map(applyMember, ApplyMemberReponseDTO.class);
                    applyMemberReponseDTO.setMember(modelMapper.map(applyMember.getMember(), MemberResponseDTO.class));
                    return applyMemberReponseDTO;
                })
                .collect(Collectors.toList());
        clubResponseDTO.setProfessor(modelMapper.map(club.getApplyClub().getProfessor(), ProfessorResponseDTO.class));
        clubResponseDTO.setMasterMember(modelMapper.map(club.getApplyClub().getMember(), MemberResponseDTO.class));
        clubResponseDTO.setApplyMember(applyMemberReponseDTOList);
        return clubResponseDTO;
    }


    @Override
    public ClubResponseDTO updateMasterClubInfo(Long clubId, Long memberId, ClubRequestDTO clubRequestDTO) {
        Optional<Club> optionalClub = masterRepository.findClubByClubIdAndMemberId(clubId, memberId);
        if(optionalClub.isPresent()) {
            Club club = optionalClub.get();
            modelMapper.map(clubRequestDTO, club);
            masterRepository.save(club);
            return getClubResponseDTO(club);
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_ENTITY.getMessage());
    }
}
