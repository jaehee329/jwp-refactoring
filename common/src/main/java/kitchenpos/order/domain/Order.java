package kitchenpos.order.domain;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.order.exception.OrderStatusNotChangeableException;

@Table(name = "Orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "ordered_time")
    private LocalDateTime orderedTime;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    private Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    protected Order() {
    }

    public static Order of(Long orderTableId, OrderStatus orderStatus,
            LocalDateTime orderedTime,
            List<MenuIdQuantityAndPrice> menuIdQuantityAndPrices) {
        return new Order(orderTableId, orderStatus, orderedTime, menuIdQuantityAndPrices.stream()
                .map(menuIdQuantityAndPrice -> new OrderLineItem(menuIdQuantityAndPrice.getMenuId(),
                        menuIdQuantityAndPrice.getQuantity(),
                        menuIdQuantityAndPrice.getOrderedPrice()))
                .collect(toList()));
    }

    public void changeOrderStatus(String orderStatus) {
        OrderStatus status = OrderStatus.resolve(orderStatus);
        validateDoesStatusChangeable();
        this.orderStatus = status;
    }

    private void validateDoesStatusChangeable() {
        if (Objects.equals(orderStatus, OrderStatus.COMPLETION)) {
            throw new OrderStatusNotChangeableException();
        }
    }

    public boolean isOrderUnCompleted() {
        return !Objects.equals(orderStatus, OrderStatus.COMPLETION);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public static class MenuIdQuantityAndPrice {

        private final Long menuId;
        private final Long quantity;
        private final BigDecimal orderedPrice;

        public MenuIdQuantityAndPrice(Long menuId, Long quantity, BigDecimal orderedPrice) {
            this.menuId = menuId;
            this.quantity = quantity;
            this.orderedPrice = orderedPrice;
        }

        public Long getMenuId() {
            return menuId;
        }

        public Long getQuantity() {
            return quantity;
        }

        public BigDecimal getOrderedPrice() {
            return orderedPrice;
        }
    }
}
