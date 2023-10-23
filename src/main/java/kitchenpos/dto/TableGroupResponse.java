package kitchenpos.dto;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {

    private final Long id;

    private final LocalDateTime createdDate;

    private final List<OrderTableDto> orderTables;

    private TableGroupResponse(Long id, LocalDateTime createdDate,
            List<OrderTableDto> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
                tableGroup.getOrderTables().stream().map(OrderTableDto::from).collect(
                        toList()));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableDto {

        private final Long id;
        private final Integer numberOfGuests;
        private final Boolean empty;

        private OrderTableDto(Long id, Integer numberOfGuests, Boolean empty) {
            this.id = id;
            this.numberOfGuests = numberOfGuests;
            this.empty = empty;
        }

        public static OrderTableDto from(OrderTable orderTable) {
            return new OrderTableDto(orderTable.getId(), orderTable.getNumberOfGuests(),
                    orderTable.isEmpty());
        }

        public Long getId() {
            return id;
        }

        public Integer getNumberOfGuests() {
            return numberOfGuests;
        }

        public Boolean getEmpty() {
            return empty;
        }
    }
}