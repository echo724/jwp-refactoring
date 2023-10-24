package kitchenpos.ui;

import kitchenpos.application.table.TableService;
import kitchenpos.dto.table.ChangeNumberOfGuestsRequest;
import kitchenpos.dto.table.ChangeOrderTableOrderableRequest;
import kitchenpos.dto.table.CreateOrderTableRequest;
import kitchenpos.dto.table.ListOrderTableResponse;
import kitchenpos.dto.table.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody final CreateOrderTableRequest orderTable) {
        final OrderTableResponse created = tableService.create(orderTable);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<ListOrderTableResponse> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/orderable")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final long orderTableId,
            @RequestBody final ChangeOrderTableOrderableRequest orderableRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeOrderable(orderTableId, orderableRequest));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final long orderTableId,
            @RequestBody final ChangeNumberOfGuestsRequest numberOfGuestsRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, numberOfGuestsRequest));
    }
}
