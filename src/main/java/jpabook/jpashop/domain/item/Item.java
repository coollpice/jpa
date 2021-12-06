package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // [상속관계맵핑전략]
@DiscriminatorColumn(name = "dtype") // [싱글테이블 전략 구분용]
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직 ( 도메인주도 설계 )==//

    //재고수량 증가
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    //재고수량 감소
    public void removeStock(int quantity) throws NotEnoughStockException {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException
                    ("need more stock"); //Custom Exception
        }
        this.stockQuantity = restStock;
    }
}
