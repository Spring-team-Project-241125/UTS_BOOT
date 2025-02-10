package com.mbc.controller;

import com.mbc.dto.ItemSearchDto;
import com.mbc.dto.MainItemDto;
import com.mbc.entity.Member;
import com.mbc.service.ItemService;
import com.mbc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;
    private final MemberRepository memberRepository;  // MemberRepository를 주입

    @GetMapping(value = {"/", "/{page}"})
    public String main(ItemSearchDto itemSearchDto,
                       @PathVariable("page") Optional<Integer> page,
                       Authentication authentication, Model model) {

        // 페이지 설정
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);

        // 아이템 목록 가져오기
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        // 로그인한 사용자 정보 가져오기
        if (authentication != null) {
            // authentication에서 PrincipalDetails 객체를 안전하게 가져오기 위해 확인
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                User user = (User) principal;
                String username = user.getUsername();  // 로그인한 사용자의 username을 추출

                // DB에서 username을 사용하여 Member 객체 조회
               Member member = memberRepository.findByname(username);
               System.out.println("Member:" + member);
               System.out.println("memberId:" + member.getId());
               model.addAttribute("memberId", member.getId());
            }
        }

        return "main";
    }
}
