package kitchenpos.menu.domain;

import static kitchenpos.menu.domain.MenuGroupTestFixture.세트류;
import static kitchenpos.menu.domain.MenuProductTestFixture.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;

public class MenuTestFixture {

    public static Menu 짜장_탕수육_세트 = menu(1L, "짜장_탕수육_세트", BigDecimal.valueOf(25_000L), 세트류.id(), Arrays.asList(짜장면_1그릇, 탕수육_소_1그릇));
    public static Menu 짬뽕2_탕수육_세트 = menu(2L, "짬뽕2_탕수육_세트", BigDecimal.valueOf(30_000L), 세트류.id(), Arrays.asList(짬뽕_2그릇, 탕수육_소_1그릇));

    public static MenuRequest menuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }

    public static Menu menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of(id, name, price, menuGroupId, menuProducts);
    }

    public static Menu menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of(name, price, menuGroupId, menuProducts);
    }
}