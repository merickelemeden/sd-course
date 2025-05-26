package com.sd_project.sd_course.service;

import com.sd_project.sd_course.entity.Role;
import com.sd_project.sd_course.entity.Category;
import com.sd_project.sd_course.entity.Product;
import com.sd_project.sd_course.repository.RoleRepository;
import com.sd_project.sd_course.repository.CategoryRepository;
import com.sd_project.sd_course.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitializationService implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeCategories();
        initializeProducts();
    }

    private void initializeRoles() {
        log.info("Initializing roles...");
        
        for (Role.RoleName roleName : Role.RoleName.values()) {
            if (!roleRepository.existsByName(roleName)) {
                Role role = Role.builder()
                        .name(roleName)
                        .build();
                roleRepository.save(role);
                log.info("Created role: {}", roleName);
            }
        }
        
        log.info("Roles initialization completed");
    }

    private void initializeCategories() {
        log.info("Initializing sample categories...");
        
        if (categoryRepository.count() == 0) {
            String[][] categoryData = {
                    {"Electronics", "Electronic devices and gadgets"},
                    {"Clothing", "Fashion and apparel"},
                    {"Books", "Books and literature"},
                    {"Home & Garden", "Home improvement and gardening supplies"},
                    {"Sports", "Sports equipment and accessories"},
                    {"Beauty & Health", "Beauty products and health supplements"},
                    {"Toys & Games", "Toys, games, and entertainment"},
                    {"Automotive", "Car parts and automotive accessories"}
            };

            for (String[] data : categoryData) {
                Category category = Category.builder()
                        .name(data[0])
                        .description(data[1])
                        .build();
                categoryRepository.save(category);
                log.info("Created category: {}", data[0]);
            }
        }
        
        log.info("Categories initialization completed");
    }

    private void initializeProducts() {
        log.info("Initializing sample products...");
        
        if (productRepository.count() == 0) {
            List<Category> categories = categoryRepository.findAllByOrderByNameAsc();
            
            for (Category category : categories) {
                createProductsForCategory(category);
            }
        }
        
        log.info("Products initialization completed");
    }

    private void createProductsForCategory(Category category) {
        String categoryName = category.getName();
        
        switch (categoryName) {
            case "Electronics":
                createElectronicsProducts(category);
                break;
            case "Clothing":
                createClothingProducts(category);
                break;
            case "Books":
                createBooksProducts(category);
                break;
            case "Home & Garden":
                createHomeGardenProducts(category);
                break;
            case "Sports":
                createSportsProducts(category);
                break;
            case "Beauty & Health":
                createBeautyHealthProducts(category);
                break;
            case "Toys & Games":
                createToysGamesProducts(category);
                break;
            case "Automotive":
                createAutomotiveProducts(category);
                break;
            default:
                log.debug("No sample products defined for category: {}", categoryName);
        }
    }

    private void createElectronicsProducts(Category category) {
        Object[][] products = {
                {"iPhone 15 Pro", "Latest iPhone with advanced camera system and A17 Pro chip", new BigDecimal("999.99"), 50},
                {"Samsung Galaxy S24", "Premium Android smartphone with AI features", new BigDecimal("899.99"), 75},
                {"MacBook Air M3", "Lightweight laptop with M3 chip and 13-inch display", new BigDecimal("1299.99"), 30},
                {"Dell XPS 13", "Ultra-portable Windows laptop with premium build quality", new BigDecimal("1199.99"), 25},
                {"iPad Pro 12.9", "Professional tablet with M2 chip and Liquid Retina display", new BigDecimal("1099.99"), 40},
                {"Sony WH-1000XM5", "Premium noise-canceling wireless headphones", new BigDecimal("399.99"), 100},
                {"Apple Watch Series 9", "Advanced smartwatch with health monitoring", new BigDecimal("399.99"), 60},
                {"Nintendo Switch OLED", "Portable gaming console with OLED screen", new BigDecimal("349.99"), 80},
                {"AirPods Pro 2", "Wireless earbuds with active noise cancellation", new BigDecimal("249.99"), 120},
                {"Samsung 55\" 4K TV", "Smart TV with Crystal UHD display and Tizen OS", new BigDecimal("699.99"), 20}
        };
        
        for (Object[] productData : products) {
            createProduct((String) productData[0], (String) productData[1], 
                         (BigDecimal) productData[2], (Integer) productData[3], category);
        }
    }

    private void createClothingProducts(Category category) {
        Object[][] products = {
                {"Levi's 501 Jeans", "Classic straight-fit denim jeans in vintage wash", new BigDecimal("89.99"), 150},
                {"Nike Air Force 1", "Iconic basketball sneakers in white leather", new BigDecimal("110.00"), 200},
                {"Adidas Hoodie", "Comfortable cotton blend hoodie with logo", new BigDecimal("65.00"), 100},
                {"H&M Basic T-Shirt", "Essential cotton t-shirt in various colors", new BigDecimal("12.99"), 300},
                {"Zara Blazer", "Professional blazer for business and casual wear", new BigDecimal("79.99"), 50},
                {"Uniqlo Down Jacket", "Lightweight and warm winter jacket", new BigDecimal("99.99"), 75},
                {"Gap Chinos", "Versatile chino pants for everyday wear", new BigDecimal("49.99"), 120},
                {"Converse Chuck Taylor", "Classic canvas sneakers in high-top style", new BigDecimal("65.00"), 180},
                {"Ralph Lauren Polo", "Classic polo shirt with embroidered logo", new BigDecimal("89.99"), 90},
                {"Lululemon Leggings", "High-performance athletic leggings", new BigDecimal("128.00"), 80}
        };
        
        for (Object[] productData : products) {
            createProduct((String) productData[0], (String) productData[1], 
                         (BigDecimal) productData[2], (Integer) productData[3], category);
        }
    }

    private void createBooksProducts(Category category) {
        Object[][] products = {
                {"The Great Gatsby", "Classic American novel by F. Scott Fitzgerald", new BigDecimal("14.99"), 200},
                {"To Kill a Mockingbird", "Pulitzer Prize-winning novel by Harper Lee", new BigDecimal("16.99"), 150},
                {"1984", "Dystopian social science fiction novel by George Orwell", new BigDecimal("13.99"), 180},
                {"Pride and Prejudice", "Romantic novel by Jane Austen", new BigDecimal("12.99"), 120},
                {"The Catcher in the Rye", "Coming-of-age novel by J.D. Salinger", new BigDecimal("15.99"), 100},
                {"Harry Potter Series", "Complete 7-book series by J.K. Rowling", new BigDecimal("89.99"), 50},
                {"The Lord of the Rings", "Epic fantasy trilogy by J.R.R. Tolkien", new BigDecimal("39.99"), 75},
                {"Dune", "Science fiction novel by Frank Herbert", new BigDecimal("18.99"), 90},
                {"The Hobbit", "Fantasy adventure novel by J.R.R. Tolkien", new BigDecimal("14.99"), 110},
                {"Atomic Habits", "Self-help book by James Clear", new BigDecimal("19.99"), 200}
        };
        
        for (Object[] productData : products) {
            createProduct((String) productData[0], (String) productData[1], 
                         (BigDecimal) productData[2], (Integer) productData[3], category);
        }
    }

    private void createHomeGardenProducts(Category category) {
        Object[][] products = {
                {"Dyson V15 Vacuum", "Cordless vacuum cleaner with laser detection", new BigDecimal("749.99"), 30},
                {"KitchenAid Stand Mixer", "Professional 5-quart stand mixer", new BigDecimal("379.99"), 25},
                {"Instant Pot Duo", "7-in-1 electric pressure cooker", new BigDecimal("99.99"), 60},
                {"Philips Air Fryer", "Healthy cooking with rapid air technology", new BigDecimal("199.99"), 45},
                {"Nespresso Coffee Machine", "Premium espresso and coffee maker", new BigDecimal("299.99"), 35},
                {"Garden Hose 50ft", "Heavy-duty expandable garden hose", new BigDecimal("39.99"), 100},
                {"LED Desk Lamp", "Adjustable LED lamp with USB charging", new BigDecimal("49.99"), 80},
                {"Memory Foam Pillow", "Ergonomic pillow for better sleep", new BigDecimal("59.99"), 120},
                {"Plant Pot Set", "Ceramic pots for indoor plants (set of 3)", new BigDecimal("29.99"), 150},
                {"Tool Set 100pc", "Complete home repair tool kit", new BigDecimal("89.99"), 40}
        };
        
        for (Object[] productData : products) {
            createProduct((String) productData[0], (String) productData[1], 
                         (BigDecimal) productData[2], (Integer) productData[3], category);
        }
    }

    private void createSportsProducts(Category category) {
        Object[][] products = {
                {"Wilson Tennis Racket", "Professional tennis racket for intermediate players", new BigDecimal("149.99"), 50},
                {"Nike Running Shoes", "Lightweight running shoes with air cushioning", new BigDecimal("129.99"), 100},
                {"Spalding Basketball", "Official size basketball for indoor/outdoor use", new BigDecimal("29.99"), 80},
                {"Yoga Mat Premium", "Non-slip yoga mat with carrying strap", new BigDecimal("39.99"), 120},
                {"Dumbbells Set 20kg", "Adjustable dumbbells for home workouts", new BigDecimal("199.99"), 30},
                {"Soccer Ball FIFA", "Official FIFA approved soccer ball", new BigDecimal("34.99"), 90},
                {"Swimming Goggles", "Anti-fog swimming goggles with UV protection", new BigDecimal("24.99"), 150},
                {"Resistance Bands Set", "Complete resistance training band set", new BigDecimal("29.99"), 100},
                {"Golf Club Set", "Beginner-friendly golf club set with bag", new BigDecimal("299.99"), 20},
                {"Protein Powder 2kg", "Whey protein powder for muscle building", new BigDecimal("49.99"), 75}
        };
        
        for (Object[] productData : products) {
            createProduct((String) productData[0], (String) productData[1], 
                         (BigDecimal) productData[2], (Integer) productData[3], category);
        }
    }

    private void createBeautyHealthProducts(Category category) {
        Object[][] products = {
                {"Vitamin D3 Supplements", "High-potency vitamin D3 for immune support", new BigDecimal("19.99"), 200},
                {"Face Moisturizer SPF30", "Daily moisturizer with sun protection", new BigDecimal("34.99"), 100},
                {"Electric Toothbrush", "Sonic toothbrush with multiple cleaning modes", new BigDecimal("89.99"), 60},
                {"Hair Dryer Ionic", "Professional ionic hair dryer with diffuser", new BigDecimal("79.99"), 40},
                {"Makeup Brush Set", "Professional makeup brush set (12 pieces)", new BigDecimal("49.99"), 80},
                {"Omega-3 Fish Oil", "Premium fish oil capsules for heart health", new BigDecimal("24.99"), 150},
                {"Face Serum Vitamin C", "Anti-aging serum with vitamin C and hyaluronic acid", new BigDecimal("39.99"), 90},
                {"Essential Oil Diffuser", "Ultrasonic aromatherapy diffuser", new BigDecimal("59.99"), 70},
                {"Multivitamin Gummies", "Daily multivitamin in gummy form", new BigDecimal("16.99"), 180},
                {"Skincare Set", "Complete skincare routine set for all skin types", new BigDecimal("89.99"), 50}
        };
        
        for (Object[] productData : products) {
            createProduct((String) productData[0], (String) productData[1], 
                         (BigDecimal) productData[2], (Integer) productData[3], category);
        }
    }

    private void createToysGamesProducts(Category category) {
        Object[][] products = {
                {"LEGO Creator Set", "3-in-1 creator set with 500+ pieces", new BigDecimal("79.99"), 60},
                {"Monopoly Board Game", "Classic property trading board game", new BigDecimal("29.99"), 100},
                {"Rubik's Cube 3x3", "Original Rubik's cube puzzle", new BigDecimal("14.99"), 150},
                {"Barbie Dreamhouse", "3-story dollhouse with furniture and accessories", new BigDecimal("199.99"), 25},
                {"Hot Wheels Track Set", "Action-packed racing track with loop", new BigDecimal("49.99"), 80},
                {"Puzzle 1000 Pieces", "Beautiful landscape jigsaw puzzle", new BigDecimal("19.99"), 120},
                {"Remote Control Car", "High-speed RC car for outdoor fun", new BigDecimal("89.99"), 40},
                {"Chess Set Wooden", "Handcrafted wooden chess set with board", new BigDecimal("59.99"), 50},
                {"Play-Doh Set", "Creative modeling compound set with tools", new BigDecimal("24.99"), 100},
                {"Action Figure Set", "Superhero action figures (set of 4)", new BigDecimal("39.99"), 90}
        };
        
        for (Object[] productData : products) {
            createProduct((String) productData[0], (String) productData[1], 
                         (BigDecimal) productData[2], (Integer) productData[3], category);
        }
    }

    private void createAutomotiveProducts(Category category) {
        Object[][] products = {
                {"Motor Oil 5W-30", "Full synthetic motor oil (5 quart)", new BigDecimal("34.99"), 100},
                {"Car Phone Mount", "Magnetic phone holder for dashboard", new BigDecimal("19.99"), 150},
                {"Jump Starter Portable", "Portable car battery jump starter", new BigDecimal("89.99"), 40},
                {"Car Vacuum Cleaner", "Cordless handheld car vacuum", new BigDecimal("59.99"), 60},
                {"Tire Pressure Gauge", "Digital tire pressure gauge with LED display", new BigDecimal("24.99"), 80},
                {"Car Air Freshener", "Long-lasting car air freshener (pack of 6)", new BigDecimal("12.99"), 200},
                {"Dash Cam HD", "1080p dashboard camera with night vision", new BigDecimal("129.99"), 50},
                {"Car Floor Mats", "All-weather rubber floor mats (set of 4)", new BigDecimal("49.99"), 70},
                {"Emergency Kit", "Complete roadside emergency kit", new BigDecimal("79.99"), 30},
                {"Car Wax Premium", "Professional grade car wax for shine protection", new BigDecimal("29.99"), 90}
        };
        
        for (Object[] productData : products) {
            createProduct((String) productData[0], (String) productData[1], 
                         (BigDecimal) productData[2], (Integer) productData[3], category);
        }
    }

    private void createProduct(String name, String description, BigDecimal price, Integer stockQuantity, Category category) {
        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .stockQuantity(stockQuantity)
                .category(category)
                .build();
        
        productRepository.save(product);
        log.debug("Created product: {} in category: {}", name, category.getName());
    }
} 