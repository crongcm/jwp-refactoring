package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResponse {
    private final Long id;
    private final String name;

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.id(), menuGroup.nameValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}