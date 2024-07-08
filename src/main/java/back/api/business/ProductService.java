package back.api.business;

import back.api.exceptions.BadRequestException;
import back.api.persistence.models.Product;
import back.api.persistence.models.User;
import back.api.persistence.models.enums.Category;
import back.api.persistence.models.requests.PostProduct;
import back.api.persistence.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;

    @Autowired
    public ProductService(ProductRepository productRepository, UserService userService) {
        this.productRepository = productRepository;
        this.userService = userService;
    }

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public Product getById(Long id){
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> findByCategory(String category){
        Category cat = Category.valueOf(category.toUpperCase());

        return productRepository.findProductsByCategory(cat);
    }

    public List<Product> findByPrice(double min, double max){
        return productRepository.findProductsByPriceBetween(min, max);
    }

    public List<Product> findByName(String name){
        return productRepository.findProductsByNameContains(name);
    }

    public List<Product> findByRating(int min, int max){
        return productRepository.findProductsByRatingBetween(min, max);
    }

    @Transactional
    public void remove(long id, int quantity){

        Product p = productRepository
                .findById(id)
                .orElseThrow(() -> new BadRequestException("Product not found id:" + id));

        p.setQuantity(p.getQuantity() - quantity);
        productRepository.save(p);
    }

    @Transactional
    public void add(long id, int quantity){
        Product p = productRepository
                .findById(id)
                .orElseThrow(() -> new BadRequestException("Product not found id:" + id));

        p.setQuantity(p.getQuantity() + quantity);
        productRepository.save(p);
    }

    @Transactional
    public void save(PostProduct postProduct, String url, String name) {

        User user = userService.findByUsername(name).orElseThrow(() -> new BadRequestException("User not found"));
        //System.out.println(user.getUsername());
        //System.out.println(user.getId());

        Product p = new Product(
                postProduct.name(),
                postProduct.description(),
                postProduct.price(),
                new Random().nextInt(0,6),
                postProduct.category(),
                postProduct.quantity(),
                postProduct.discount(),
                url
        );

        p.setUser(user);

        productRepository.save(p);
    }

    @Transactional
    public void update(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public void delete(Long id){
        productRepository.deleteById(id);
    }
}
