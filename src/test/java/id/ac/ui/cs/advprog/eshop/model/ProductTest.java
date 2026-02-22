package id.ac.ui.cs.advprog.eshop.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    Product product;
    Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        this.product = new Product();
        this.product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        this.product.setProductName("Sampo Cap Bambang");
        this.product.setProductQuantity(100);
    }

    @Test
    void testGetProductId() {
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", this.product.getProductId());
    }

    @Test
    void testGetProductName() {
        assertEquals("Sampo Cap Bambang", this.product.getProductName());
    }

    @Test
    void testGetProductQuantity() {
        assertEquals(100, this.product.getProductQuantity());
    }

    @Test
    void testQuantityShouldBeValidWhenNonNegative() {
        product.setProductQuantity(0);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testQuantityShouldBeInvalidWhenNegative() {
        product.setProductQuantity(-1);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertFalse(violations.isEmpty());
        boolean hasMinViolation = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Quantity tidak boleh negatif"));
        assertTrue(hasMinViolation);
    }
}