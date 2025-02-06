package com.mbc.service;

import com.mbc.constant.ItemStatus;
import com.mbc.dto.ItemFormDto;
import com.mbc.entity.Category;
import com.mbc.entity.Member;
import com.mbc.repository.CategoryRepository;
import com.mbc.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Commit
    void testSaveMultipleItems() throws Exception {
        // 모든 멤버를 가져오기
        List<Member> members = memberRepository.findAll();

        // 카테고리 가져오기
        List<Category> categories = categoryRepository.findAll();

        // 최하위 카테고리만 필터링
        List<Category> leafCategories = categories.stream()
                .filter(category -> category.getParent() != null && categories.stream().noneMatch(c -> c.getParent() != null && c.getParent().equals(category)))
                .collect(Collectors.toList());

        // 랜덤 객체 생성
        Random random = new Random();

        // 50개의 상품을 생성하여 저장
        for (int i = 1; i <= 50; i++) {
            // 랜덤으로 멤버를 선택
            Member selectedMember = members.get(random.nextInt(members.size()));

            // 최하위 카테고리 중 하나를 랜덤으로 선택
            Category selectedCategory = leafCategories.get(i % leafCategories.size());

            // 배송 방식 랜덤 결정
            String shipping = random.nextBoolean() ? "free" : "separate";
            Integer shippingPrice = 0;

            // "separate"일 경우 shippingPrice를 랜덤으로 설정
            if ("separate".equals(shipping)) {
                shippingPrice = 3000 + random.nextInt(27000 / 1000) * 1000;  // 3000 ~ 30000 사이의 천 단위 값
            }

            // 직거래 가능 여부 랜덤 결정
            String tradeAvailable = random.nextBoolean() ? "possible" : "impossible";

            // 직거래 위치는 tradeAvailable이 "possible"일 때만 설정
            String tradeLocation = "possible".equals(tradeAvailable) ? "Location " + i : null;

            // 각 상품의 정보를 설정
            ItemFormDto itemFormDto = ItemFormDto.builder()
                    .itemNm("Item " + i) // 상품명
                    .price(1000 * i) // 가격
                    .itemDetail("This is the detail for item " + i) // 상품 설명
                    .stockNumber(1000) // 재고 수량
                    .itemStatus(ItemStatus.values()[i % 5]) // 상품 상태
                    .categoryId(selectedCategory.getId()) // 랜덤으로 선택된 최하위 카테고리 ID
                    .shipping(shipping) // "free" 또는 "separate"
                    .shippingPrice(shippingPrice) // 가격이 없는 경우 null
                    .tradeAvailable(tradeAvailable) // "possible" 또는 "impossible"
                    .tradeLocation(tradeLocation) // 직거래 위치
                    .memberId(selectedMember.getId()) // 랜덤으로 선택된 판매자 ID
                    .build();

            // 이미지는 빈 리스트로 전달
            List<MultipartFile> itemImgFileList = new ArrayList<>();  // 이미지가 없다고 가정

            // 상품을 저장
            itemService.saveItem(itemFormDto, itemImgFileList);
        }
    }

}
