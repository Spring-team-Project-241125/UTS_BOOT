package com.mbc.service;

import com.mbc.entity.Item;
import com.mbc.entity.Member;
import com.mbc.entity.Order;
import com.mbc.entity.Review;
import com.mbc.repository.ItemRepository;
import com.mbc.repository.MemberRepository;
import com.mbc.repository.OrderRepository;
import com.mbc.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@SpringBootTest
@Transactional
public class ReviewServiceTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Commit
    public void testGenerateReviews() {
        // 모든 멤버와 아이템을 가져옵니다.
        List<Member> members = memberRepository.findAll();
        List<Item> items = itemRepository.findAll();
        List<Order> orders = orderRepository.findAll(); // 모든 주문을 가져옴

        // 랜덤 객체 생성
        Random random = new Random();

        // 중복된 (member_id, item_id) 조합을 추적할 Set
        Set<String> generatedReviews = new HashSet<>();

        // 1000개의 리뷰를 생성하여 저장
        int reviewCount = 0;
        while (reviewCount < 1000) {
            // 랜덤으로 멤버를 선택
            Member selectedMember = members.get(random.nextInt(members.size()));

            // 랜덤으로 아이템을 선택
            Item selectedItem = items.get(random.nextInt(items.size()));

            // 동일한 멤버와 아이템에 대한 리뷰가 이미 존재하는지 체크
            String memberItemKey = selectedMember.getId() + "_" + selectedItem.getId();
            if (generatedReviews.contains(memberItemKey)) {
                continue; // 중복된 리뷰가 있으면 넘어감
            }

            // i 값에 맞는 순차적인 주문 선택 (i-1 번째 주문을 선택)
            Order selectedOrder = orders.get(reviewCount % orders.size());  // 순차적으로 `order_id`를 선택

            // 랜덤 별점 (1~5)
            int rating = random.nextInt(5) + 1;

            // 리뷰 생성
            Review review = new Review();
            review.setMemberName(selectedMember.getName()); // 랜덤으로 선택된 멤버 이름
            review.setReviewDetail("This is a review detail for review " + (reviewCount + 1)); // 리뷰 내용
            review.setRating(rating); // 랜덤 별점
            review.setItem(selectedItem); // 랜덤으로 선택된 아이템
            review.setMember(selectedMember); // 랜덤으로 선택된 멤버
            review.setOrder(selectedOrder); // 순차적으로 선택된 주문

            // 리뷰 저장
            reviewRepository.save(review);

            // 생성된 리뷰의 (member_id, item_id) 조합을 추적
            generatedReviews.add(memberItemKey);

            // 리뷰 수 증가
            reviewCount++;
        }
    }

}
