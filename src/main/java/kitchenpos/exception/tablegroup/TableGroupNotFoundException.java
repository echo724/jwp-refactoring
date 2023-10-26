package kitchenpos.exception.tablegroup;

import kitchenpos.exception.common.NotFoundException;

public class TableGroupNotFoundException extends NotFoundException {
    private static final String RESOURCE = "테이블 그룹";

    public TableGroupNotFoundException(final Long tableGroupId) {
        super(RESOURCE, tableGroupId);
    }
}
