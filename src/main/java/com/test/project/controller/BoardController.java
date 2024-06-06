package com.test.project.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.test.project.dto.BoardDTO;
import com.test.project.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    
    // 저장
    @GetMapping("/save")
    public String saveForm() {
        return "pagesave";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) {
        System.out.println("BoardDTO = "+ boardDTO);
        boardService.save(boardDTO);
        return "boardpage";
    } 

    // 홈
    @GetMapping("/")
    public String home() {
        return "boardpage";
    }
    // 게시판 리스트
    @GetMapping("/pagelist")
    public String findAll(Model model) {
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList",boardDTOList); 
        return "pagelist";
    }
    
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,@PageableDefault(page=1) Pageable pageable) {
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber()); //페이지 추가
        return "pagedetail";
    }
    
    //수정
    @GetMapping("/pageupdate/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDTO);
        return "pageupdate";
    }

    @PostMapping("/pageupdate")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);
        return "pagedetail";    
    }
    
    //삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board/";
    }


    // 게시판 페이지(1,2,3...)로 보여주는 부분
    @GetMapping("/paging")
    public String paging(@PageableDefault(page=1) Pageable pageable, Model model) {
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3; //밑에 보여지는 페이지 갯수
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "paging";
        
    }
    
    
}
