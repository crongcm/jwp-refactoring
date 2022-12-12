package kitchenpos.product.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    public ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.id(), product.nameValue(), product.priceValue());
    }

    public static List<ProductResponse> toList(List<Product> products) {
        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
