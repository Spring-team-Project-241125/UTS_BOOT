package com.mbc.dto;

import com.mbc.constant.ItemSellStatus;
import com.mbc.constant.ItemStatus;
import com.mbc.entity.Item;
import com.mbc.entity.Member;
import com.mbc.validation.TradeLocationValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TradeLocationValid // 커스텀 유효성 검사 어노테이션 추가
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품설명은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고수량은 필수 입력 값입니다.")
    private Integer stockNumber;

    @Builder.Default
    private ItemSellStatus itemSellStatus = ItemSellStatus.SELL;

    @NotNull(message = "상품상태는 필수 입력 값입니다.")
    private ItemStatus itemStatus;

    @NotNull(message = "카테고리는 필수 입력 값입니다.")
    private Long categoryId;  // 소분류 ID
    private Long parentCategoryId;  // 중분류 ID (부모 카테고리 ID)
    private Long grandparentCategoryId;  // 대분류 ID (조부모 카테고리 ID)

    // 카테고리 계층을 추가하는 필드
    private String categoryHierarchy;

    @NotNull(message = "배송비 설정은 필수 입력 값입니다.")
    public String shipping; // 배송

    @Builder.Default
    public Integer shippingPrice=0; //배송비

    @NotNull(message = "직거래 가능여부는 필수 입력 값입니다.")
    public String tradeAvailable; // "possible" 또는 "impossible"

    // 직거래 위치
    public String tradeLocation;  // 직거래 위치 정보

    private String userName; // 상품을 등록한 유저의 이름

    @Builder.Default
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private Long memberId; // member_id 추가



    private static ModelMapper modelMapper = new ModelMapper();

    // 판매자와 구매자 설정하는 메서드
    public Item createItem(Member member) {
        Item item = modelMapper.map(this, Item.class);
        item.setMember(member);  // 판매자 설정
        return item;
    }

    public static ItemFormDto of(Item item) {
        ItemFormDto itemFormDto = modelMapper.map(item, ItemFormDto.class);
        // 카테고리 계층을 설정
        itemFormDto.setCategoryHierarchy(item.getCategoryHierarchy());
        // 유저의 이름을 가져와 설정
        itemFormDto.setUserName(item.getCreatedBy());

        return itemFormDto;
    }


    // itemStatus를 한글로 반환하는 메서드
    public String getItemStatusDescription() {
        return (itemStatus != null) ? itemStatus.getDescription() : "상품 상태 정보 없음";

    }

}
