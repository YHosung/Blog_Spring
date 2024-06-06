package com.test.project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.test.project.dto.MemberDTO;
import com.test.project.entity.MemberEntity;
import com.test.project.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void save(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    public MemberDTO login(MemberDTO memberDTO) {
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if (byMemberEmail.isPresent()) {
            MemberEntity memberEntity = byMemberEmail.get();
            if (memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                // 비밀번호 일치
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            } else {
                // 비밀번호 불일치
                return null;
            }
        } else {
            // 조회 결과 없음
            return null;
        }
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity : memberEntityList) {
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if (optionalMemberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }

    public MemberDTO findByEmail(String email) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(email);
        return optionalMemberEntity.map(MemberDTO::toMemberDTO).orElse(null);
    }

    public MemberDTO updateForm(String myEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(myEmail);
        if (optionalMemberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }

    public boolean checkPassword(String email, String password) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(email);
        return optionalMemberEntity.map(memberEntity -> memberEntity.getMemberPassword().equals(password)).orElse(false);
    }
    
    public void update(MemberDTO memberDTO) {
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    // 이메일로 회원 탈퇴 메서드 수정
    public void deleteByEmail(String email) {
        memberRepository.deleteByMemberEmail(email);
    }
}
