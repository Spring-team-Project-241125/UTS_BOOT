package com.mbc.service;

import com.mbc.dto.MemberFormDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @Commit
    void testSaveMultipleMembers() throws Exception {
        // 50명의 멤버를 생성하기 위한 루프
        for (int i = 1; i <= 50; i++) {
            // MemberFormDto 객체를 빌더 패턴으로 생성
            MemberFormDto memberFormDto = MemberFormDto.builder()
                    .name("user" + i)
                    .email("user" + i + "@example.com")
                    .password("1234")
                    .address("Address " + i)
                    .phone("010-1234-567" + i)
                    .build();  // 빌더 패턴으로 객체 생성

            // MemberService의 saveMember 메서드를 호출하여 멤버를 저장
            memberService.saveMember(memberFormDto, null); // 이미지 없이 호출
        }
    }
}
