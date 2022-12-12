package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductTestFixture {
    public static final Product 짜장면 = product(1L, "짜장면", BigDecimal.valueOf(7000));
    public static final Product 짬뽕 = product(2L, "짬뽕", BigDecimal.valueOf(8000));

    public static Product product(Long id, String name, BigDecimal price) {
        return Product.of(id, name, price);
    }
}
