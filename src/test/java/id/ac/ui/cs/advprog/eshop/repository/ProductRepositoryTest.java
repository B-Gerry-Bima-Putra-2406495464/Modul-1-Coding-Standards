package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest {

    private ProductRepository productRepository;
    private Product productTest;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
        productTest = new Product();
        productTest.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd1");
        productTest.setProductName("Sampo Cap Bambang");
        productTest.setProductQuantity(100);
    }

    private void setUpEditDelete() {
        productRepository.create(productTest);
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());

        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testCreateShouldGenerateIdWhenNull() {
        Product product = new Product();
        product.setProductId(null);
        product.setProductName("Tanpa ID");
        product.setProductQuantity(1);

        Product created = productRepository.create(product);

        assertNotNull(created.getProductId());
        assertFalse(created.getProductId().isEmpty());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());

        Product savedProduct1 = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct1.getProductId());

        Product savedProduct2 = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct2.getProductId());

        assertFalse(productIterator.hasNext());
    }

    @Test
    void testEditProduct() {
        setUpEditDelete();

        Product updatedProduct = new Product();
        updatedProduct.setProductId(productTest.getProductId());
        updatedProduct.setProductName("Sampo Cap Usep");
        updatedProduct.setProductQuantity(50);

        Product updated = productRepository.update(updatedProduct);
        assertNotNull(updated);

        Product result = productRepository.findById(productTest.getProductId());
        assertNotNull(result);
        assertEquals("Sampo Cap Usep", result.getProductName());
        assertEquals(50, result.getProductQuantity());
    }

    @Test
    void testEditProductNotFound() {
        setUpEditDelete();

        Product updatedProduct = new Product();
        updatedProduct.setProductId("tidak-ada");
        updatedProduct.setProductName("Sampo Cap Usep");
        updatedProduct.setProductQuantity(50);

        assertNull(productRepository.update(updatedProduct));
    }

    @Test
    void testUpdateNullProductShouldReturnNull() {
        assertNull(productRepository.update(null));
    }

    @Test
    void testDeleteProduct() {
        setUpEditDelete();

        Product deleted = productRepository.delete(productTest);
        assertNotNull(deleted);

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteProductNotFound() {
        Product product = new Product();
        product.setProductId("not-in-repo");

        assertNull(productRepository.delete(product));
    }

    @Test
    void testFindById() {
        setUpEditDelete();

        Product productFound = productRepository.findById(productTest.getProductId());
        assertSame(productTest, productFound);
    }

    @Test
    void testFindByIdNotFound() {
        setUpEditDelete();

        Product productFound = productRepository.findById("tidak-ada");
        assertNull(productFound);
    }
}