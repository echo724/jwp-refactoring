package kitchenpos.domain.order;

import kitchenpos.domain.common.Quantity;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Embeddable
public class OrderLineItems {
    public static final String ORDER_LINE_ITEMS_IS_EMPTY_ERROR_MESSAGE = "주문 항목이 존재하지 않습니다.";
    public static final String MENU_AND_QUANTITY_SIZE_NOT_MATCH_ERROR_MESSAGE = "메뉴와 수량의 개수가 일치하지 않습니다.";
    @NotNull
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
    }

    private OrderLineItems(final List<OrderLineItem> orderLineItems) {
        validateEmptyOrderLineItems(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validateEmptyOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException(ORDER_LINE_ITEMS_IS_EMPTY_ERROR_MESSAGE);
        }
    }

    public static OrderLineItems of(final List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    public static OrderLineItems from(final List<Long> menuIds, final List<Quantity> quantities) {
        validateOrderLineItems(menuIds, quantities);
        return new OrderLineItems(IntStream.range(0, menuIds.size())
                .mapToObj(i -> OrderLineItem.of(menuIds.get(i), quantities.get(i)))
                .collect(Collectors.toList()));
    }

    private static void validateOrderLineItems(final List<Long> menuIds, final List<Quantity> quantities) {
        if (menuIds.size() != quantities.size()) {
            throw new IllegalArgumentException(MENU_AND_QUANTITY_SIZE_NOT_MATCH_ERROR_MESSAGE);
        }
    }

    public void setOrder(final Order order) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(order));
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public int size() {
        return orderLineItems.size();
    }
}
