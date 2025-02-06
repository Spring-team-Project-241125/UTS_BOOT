package com.mbc.service;

import com.mbc.dto.OrderDto;
import com.mbc.entity.Item;
import com.mbc.entity.Member;
import com.mbc.repository.ItemRepository;
import com.mbc.repository.MemberRepository;
import com.mbc.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Commit
    public void testGenerateOrders() {
        // 전체 아이템 목록 준비
        List<Item> items = itemRepository.findAll();
        if (items.isEmpty()) {
            throw new IllegalStateException("아이템이 없습니다.");
        }

        // 1000개의 주문 DTO 생성 및 개별 주문 생성
        for (int i = 0; i < 1000; i++) {
            // 랜덤으로 사용자 선택
            List<Member> members = memberRepository.findAll();
            if (members.isEmpty()) {
                throw new IllegalStateException("사용자가 없습니다.");
            }

            Random random = new Random();
            Member randomMember = members.get(random.nextInt(members.size()));  // 랜덤 사용자 선택

            // 랜덤으로 아이템 선택
            Item selectedItem = items.get(random.nextInt(items.size()));

            // 랜덤 주문 수량 (1~5개)
            int count = random.nextInt(5) + 1;

            // OrderDto 객체 생성
            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(selectedItem.getId());
            orderDto.setCount(count);

            // 임시 impUid와 merchantUid 설정
            String impUid = "testImpUid" + System.currentTimeMillis();
            String merchantUid = "testMerchantUid" + System.currentTimeMillis();

            // 각 주문마다 새로운 주문 생성
            Long orderId = orderService.order(orderDto, randomMember.getName(), impUid, merchantUid);

            // 생성된 주문 ID 확인
            System.out.println("생성된 주문 ID: " + orderId + " 사용자: " + randomMember.getName());
        }
    }

}
