package kitchenpos.application.integration;

import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.menu.MenuGroupService;
import kitchenpos.application.menu.MenuService;
import kitchenpos.application.order.OrderService;
import kitchenpos.application.table.TableService;
import kitchenpos.domain.common.Money;
import kitchenpos.dto.menu.CreateMenuGroupRequest;
import kitchenpos.dto.menu.CreateMenuRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.order.CreateOrderRequest;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.product.CreateProductRequest;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.dto.table.CreateOrderTableRequest;
import kitchenpos.dto.table.OrderTableResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@Sql("/db/truncate.sql")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public abstract class ApplicationIntegrationTest {
    @Autowired
    protected ProductService productService;
    @Autowired
    protected MenuService menuService;
    @Autowired
    protected MenuGroupService menuGroupService;
    @Autowired
    protected OrderService orderService;
    @Autowired
    protected TableGroupService tableGroupService;
    @Autowired
    protected TableService tableService;


    protected ProductResponse createProduct(final String name, final BigDecimal price) {
        return productService.create(CreateProductRequest.of(name, price.longValue()));
    }

    protected MenuGroupResponse createMenuGroup(final String name) {
        return menuGroupService.create(CreateMenuGroupRequest.of(name));
    }

    protected MenuResponse createMenu(final String name, final Money price) {
        final MenuGroupResponse menuGroup = createMenuGroup("치킨");
        final ProductResponse product1 = createProduct("후라이드", BigDecimal.valueOf(16000));
        final ProductResponse product2 = createProduct("양념치킨", BigDecimal.valueOf(16000));
        final MenuProductRequest menuProduct1 = MenuProductRequest.of(product1.getId(), 1);
        final MenuProductRequest menuProduct2 = MenuProductRequest.of(product2.getId(), 1);
        return menuService.create(CreateMenuRequest.of(name, price.getPrice().longValue(), menuGroup.getId(), List.of(menuProduct1, menuProduct2)));
    }

    protected OrderResponse createOrder(final long orderTableId) {
        final MenuResponse menu = createMenu("후라이드", Money.valueOf(16000));
        return orderService.create(CreateOrderRequest.of(orderTableId, List.of(OrderLineItemRequest.of(menu.getId(), 1))));
    }

    protected OrderTableResponse createOrderTable(final int numberOfGuests, final boolean orderable) {
        return tableService.create(CreateOrderTableRequest.of(numberOfGuests, orderable));
    }
}