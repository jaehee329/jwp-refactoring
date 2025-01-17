package kitchenpos.table.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.exception.RequestOrderTableCountNotEnoughException;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(OrderTableCreateRequest request) {
        OrderTable orderTable = orderTableRepository.save(
                new OrderTable(request.getNumberOfGuests(), request.getEmpty()));
        return OrderTableResponse.from(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(toList());
    }

    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableChangeEmptyRequest request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);

        orderTable.changeEmpty(request.getEmpty());

        return OrderTableResponse.from(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
            OrderTableChangeNumberOfGuestsRequest request) {
        if (request.getNumberOfGuests() < 0) {
            throw new RequestOrderTableCountNotEnoughException();
        }

        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);

        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.from(orderTable);
    }
}
