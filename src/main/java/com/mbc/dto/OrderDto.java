package com.mbc.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderDto {

    @NotNull(message = "상품 아이디는 필수 입력 값입니다.")
    private Long itemId;

    @Min(value = 1, message = "최소 주문 수량은 1개 입니다.")
    @Max(value = 999, message = "최대 주문 수량은 999개 입니다.")
    private int count;

    // 결제 정보
    String impUid;  // 아임포트 결제 uid
    String merchantUid;  // 주문 번호

    // 기본 생성자 추가
    public OrderDto() {
    }

    // 파라미터를 받는 생성자
    public OrderDto(Long itemId, int count) {
        this.itemId = itemId;
        this.count = count;
    }
}
