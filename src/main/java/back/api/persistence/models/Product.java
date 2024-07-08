package back.api.persistence.models;

import back.api.persistence.models.enums.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String description;

    private double price;

    private int rating;

    private Category category;

        private int quantity;

    private int discount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String imgUrl;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Product(String name,
                   String description,
                   Double price,
                   int rating,
                   Category category,
                   int quantity,
                   int discount,
                   String imgUrl) {

        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.category = category;
        this.quantity = quantity;
        this.discount = discount;
        this.imgUrl = imgUrl;
    }

}
