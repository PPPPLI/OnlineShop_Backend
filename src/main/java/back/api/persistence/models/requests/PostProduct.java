package back.api.persistence.models.requests;

import back.api.persistence.models.enums.Category;

public record PostProduct(String name,
                          String description,
                          double price,
                          int rating,
                          Category category,
                          int quantity,
                          int discount) {
}
