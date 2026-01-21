package com.aziz.product_service.util;

import com.aziz.product_service.model.Product;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class ProductModelListener implements BeforeConvertCallback<Product> {
    private static final int LENGTH = 60;
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public Product onBeforeConvert(Product product, String collection) {
        if (product.getId() == null) {
            product.setId(generateProductId());
        }

        if (product.getSlug() == null || product.getSlug().isEmpty()) {
            String slug = generateSlug(product.getName());
            product.setSlug(slug + "-" + product.getId());
        }

        return product;
    }

    private String generateProductId() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < LENGTH; i++) {
            sb.append(CHAR_POOL.charAt(secureRandom.nextInt(CHAR_POOL.length())));
        }

        return sb.toString();
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // remove special chars
                .replaceAll("\\s+", "-")         // replace spaces with hyphens
                .replaceAll("-+", "-");          // collapse multiple hyphens
    }
}
