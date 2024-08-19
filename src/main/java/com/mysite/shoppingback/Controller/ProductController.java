package com.mysite.shoppingback.Controller;

import com.mysite.shoppingback.Entity.Product;
import com.mysite.shoppingback.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    //유저의 제품 리스트
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // 특정 상품 조회
    @GetMapping("/products/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product != null) {
            model.addAttribute("product", product);
            return "/product/productDetail";
        } else {
            return "error/404";
        }
    }

    //관리자
    //관리자의 제품 리스트
    @GetMapping("/admin/product")
    public ResponseEntity<List<Product>> adminProduct() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

//    @GetMapping("/admin/product/new/pro")
//    public ResponseEntity<Product> newProduct() {
//        return ResponseEntity.ok(new Product());
//    }

    // 상품 등록
    @PostMapping("/admin/product/new/pro")
    public ResponseEntity<?> createProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("stock") int stock,
            @RequestParam("inSoldout") boolean inSoldout,
            @RequestParam("image") MultipartFile image) {

        String imageUrl = null;
        if (!image.isEmpty()) {
            try {
                String filename = image.getOriginalFilename();
                Path path = Paths.get("C:\\upload", filename);
                Files.write(path, image.getBytes());
                imageUrl = "/images/" + filename; // 웹에서 접근 가능한 경로
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
            }
        }

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setInSoldout(inSoldout);
        product.setImage(imageUrl);

        productService.saveProduct(product);
        return ResponseEntity.ok("Product created successfully");
    }


    // 수정하기
    @GetMapping("/admin/product/modify/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/admin/product/modify/{id}")
    public ResponseEntity<?> modifyProduct(
            @PathVariable("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("stock") int stock,
            @RequestParam("inSoldout") boolean inSoldout,
            @RequestParam("image") MultipartFile image) {

        String imageUrl = null;
        if (!image.isEmpty()) {
            try {
                String filename = image.getOriginalFilename();
                Path path = Paths.get("C:\\upload", filename);
                Files.write(path, image.getBytes());
                imageUrl = "/images/" + filename; // 웹에서 접근 가능한 경로
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
            }
        }

        Product product = productService.getProductById(id);

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setInSoldout(inSoldout);
        product.setImage(imageUrl);

        productService.saveProduct(product);
        return ResponseEntity.ok(product);
    }

    // 상품 삭제
    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
