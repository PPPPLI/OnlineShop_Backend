package back.api.persistence.repositories;

import back.api.persistence.models.Product;
import back.api.persistence.models.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {


    List<Product> findProductsByCategory(Category category);

    List<Product> findProductsByPriceBetween(double min, double max);

    List<Product> findProductsByRatingBetween(int min, int max);

    List<Product> findProductsByNameContains(String name);

}
