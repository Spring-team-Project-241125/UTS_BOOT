package com.mbc.dto;


import com.mbc.entity.Review;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewFormDto {

    private Long id;

    private String memberName; //등록자명

    private String reviewDetail; //후기 내용

    private String itemName; // 상품명 추가

    private Long itemId;

    private int rating; // 별점 추가 (1~5)

    private Long orderId; // 주문 ID 추가

    @Builder.Default
    private List<ReviewImgDto> reviewImgDtoList = new ArrayList<>();

    @Builder.Default
    private List<MultipartFile> reviewImgs = new ArrayList<>();  // 업로드된 이미지 파일 리스트 추가
}
