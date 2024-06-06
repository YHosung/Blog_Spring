package com.test.project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.project.dto.MemberDTO;
import com.test.project.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 회원가입 페이지
    @GetMapping("/member/saveid")
    public String saveForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "saveid";
    }

    @PostMapping("/member/saveid")
    public String save(@ModelAttribute MemberDTO memberDTO, RedirectAttributes ra) {
        // 이미 가입된 이메일인지 확인
        MemberDTO existingMember = memberService.findByEmail(memberDTO.getMemberEmail());
        if (existingMember != null) {
            // 이미 가입된 경우
            ra.addFlashAttribute("message", "이미 가입된 아이디입니다.");
            return "redirect:/member/saveid";
        }

        // 가입 처리
        memberService.save(memberDTO);
        return "login";
    }

    // 로그인 페이지
    @GetMapping("/member/login")
    public String loginForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO()); // 로그인 폼을 위한 빈 객체 추가
        return "login";
    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session, RedirectAttributes ra) {
        if (memberDTO.getMemberEmail() == null || memberDTO.getMemberPassword() == null) {
            // 이메일 또는 비밀번호가 null인 경우
            ra.addFlashAttribute("message", "아이디 또는 비밀번호를 입력해주세요.");
            return "redirect:/member/login";
        }

        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            // login 성공
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            return "redirect:/main";
        } else {
            // login 실패
            ra.addFlashAttribute("message", "아이디 또는 비밀번호를 잘못 입력했습니다.\n" +
                    "입력하신 내용을 다시 확인해주세요.");
            return "redirect:/member/login";
        }
    }

    // 회원 리스트 조회
    @GetMapping("/member/")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "list";
    }

    // 회원 상세 정보 조회
    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "detail";
    }

    // 회원 정보 수정 폼
    @GetMapping("/member/update")
    public String updateForm(HttpSession session, Model model) {
        String myEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.findByEmail(myEmail);
        model.addAttribute("updateMember", memberDTO);
        return "update";
    }

    // 회원 정보 수정 처리
    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO, 
                         @RequestParam String currentPassword, 
                         RedirectAttributes ra) {

        if (!memberService.checkPassword(memberDTO.getMemberEmail(), currentPassword)) {
            ra.addFlashAttribute("error", "현재 비밀번호가 일치하지 않습니다.");
            return "redirect:/member/update";
        }

        memberService.update(memberDTO);
        return "redirect:/member/" + memberDTO.getId();
    }

    // 회원 삭제
    @GetMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/member/";
    }
    // 회원 삭제
    @PostMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id, HttpSession session) {
        memberService.deleteById(id);
        session.invalidate(); // 회원 탈퇴 후 로그아웃 처리
        return "redirect:/member/login";
    }


    // 로그아웃
    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    // 메인 페이지
    @GetMapping("/main")
    public String mainPage(HttpSession session, Model model) {
        String loginEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.findByEmail(loginEmail);
        model.addAttribute("member", memberDTO);
        return "main";
    }
}
