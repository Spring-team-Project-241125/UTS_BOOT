package com.mbc.entity;

import com.mbc.constant.ItemSellStatus;
import com.mbc.constant.ItemStatus;
import com.mbc.dto.ItemFormDto;
import exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemNm; //상품명

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber; //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus; //상품 상태

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    //private LocalDateTime regTime;
    //private LocalDateTime updateTime;

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

//    private static ModelMapper modelMapper = new ModelMapper();
//
//    public void updateItem(ItemFormDto itemFormDto) {
//        modelMapper.map(itemFormDto, this);  // ModelMapper를 사용하여 DTO를 엔티티로 매핑
//    }

    //updateItem 메서드는 인스턴스 상태를 수정하는 작업이기 때문에 static으로 선언하지 않고,
    //해당 객체에서 직접 호출하는 인스턴스 메서드로 두는 것

    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;
        if(restStock < 0){
            throw new OutOfStockException("상품의 재고가 부족 합니다.(현재 재고 수량: " +
                    this.stockNumber + ")");
        }
        this.stockNumber = restStock;

        // 재고가 0이면 상태를 SOLD_OUT으로 변경
        if (this.stockNumber == 0) {
            this.itemSellStatus = ItemSellStatus.SOLD_OUT;
        }
    }

    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }


}