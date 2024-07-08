package back.api.web;

import back.api.business.ImageService;
import back.api.business.ProductService;
import back.api.persistence.models.Product;
import back.api.persistence.models.requests.PatchProduct;
import back.api.persistence.models.requests.PostProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ObjectMapper objectMapper;

    private final Logger logger = Logger.getLogger(ResourceController.class.getName());
    private final ProductService productService;
    private final ImageService imageService;

    @Autowired
    ProductController(ProductService productService, ImageService imageService){
        this.productService = productService;
        this.imageService = imageService;
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getProducts(@RequestParam(required = false, defaultValue = "0") Double minPrice,
                                         @RequestParam(required = false, defaultValue = "12000") Double maxPrice,
                                         @RequestParam(required = false, defaultValue = "5") Integer maxRating,
                                         @RequestParam(required = false, defaultValue = "0") Integer minRating,
                                         @RequestParam(required = false) String category,
                                         @RequestParam(required = false) String name){

        List<Product> allProducts = productService.getAll();

        allProducts.retainAll(productService.findByPrice(minPrice, maxPrice));
        allProducts.retainAll(productService.findByRating(minRating, maxRating));

        if(category != null) allProducts.retainAll(productService.findByCategory(category));

        if(name != null) allProducts.retainAll(productService.findByName(name));

        return ResponseEntity.ok(allProducts);
    }

    @PatchMapping()
    public ResponseEntity<?> updateProducts(@RequestBody List<PatchProduct> pList){
        pList.forEach(p -> productService.add(p.id(), p.quantity()));
        return ResponseEntity.ok().build();
    }

    @PostMapping()
    public ResponseEntity<?> newProduct(@RequestPart("img") MultipartFile img,
                                        @RequestPart("product") PostProduct product,
                                        Authentication auth) {
        try{
            imageService.saveImage(img.getOriginalFilename(), img.getBytes());
            productService.save(product, img.getOriginalFilename(), auth.getName());
        }catch (IOException e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestPart(value = "img", required = false) MultipartFile img,
                                           @RequestPart("product") Product product) {
        try{
            if (img != null) {
                imageService.saveImage(img.getOriginalFilename(), img.getBytes());
                product.setImgUrl(img.getOriginalFilename());
            }else {
                product.setImgUrl(productService.getById(product.getId()).getImgUrl());

            }

            productService.update(product);
        }catch (IOException e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }


    @GetMapping("/{product_id}")
    public ResponseEntity<?> getProductById(@PathVariable Long product_id){
        return ResponseEntity.ok(productService.getById(product_id));
    }

    @PatchMapping("/{product_id}")
    public ResponseEntity<?> removeProduct(@PathVariable Long product_id, @RequestBody Map<String, Integer> body){
        productService.remove(product_id, body.get("quantity"));
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/add/{product_id}")
    public ResponseEntity<?> addProduct(@PathVariable Long product_id, @RequestBody Map<String, Integer> body){
        productService.add(product_id, body.get("quantity"));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{product_id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long product_id){
        productService.delete(product_id);
        return ResponseEntity.ok().build();
    }
}
