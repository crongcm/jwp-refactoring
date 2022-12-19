package kitchenpos.table.application;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private static final String ERROR_MESSAGE_ORDER_TABLE_NOT_FOUND_FORMAT = "주문 테이블을 찾을 수 없습니다. ID : %d";
    private static final String ERROR_MESSAGE_NOT_FOUND_GROUP_TABLE = "존재하지 않는 단체 테이블입니다. ID : %d";

    private final OrderTablesValidator orderTablesValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTablesValidator orderTablesValidator, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderTablesValidator = orderTablesValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        List<OrderTable> requestTables = findOrderTable(request.getOrderTables());
        TableGroup tableGroup = tableGroupRepository.save(TableGroup.from(requestTables));
        tableGroup.orderTables().updateTableGroup(tableGroup);
        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = findById(tableGroupId);
        OrderTables orderTables = tableGroup.orderTables();
        orderTables.validateCookingAndMeal(orderTablesValidator);
        orderTables.ungroup();
    }

    public TableGroup findById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_GROUP_TABLE, tableGroupId)));
    }

    private List<OrderTable> findOrderTable(List<Long> orderTableIds) {
        return orderTableIds.stream()
                .map(this::findTableById)
                .collect(Collectors.toList());
    }

    private OrderTable findTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_ORDER_TABLE_NOT_FOUND_FORMAT, id)));
    }
}