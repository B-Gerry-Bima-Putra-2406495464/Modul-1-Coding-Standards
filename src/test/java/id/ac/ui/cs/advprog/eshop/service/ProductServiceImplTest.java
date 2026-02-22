package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void createShouldCallRepositoryAndReturnProduct() {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Buku");
        product.setProductQuantity(10);

        Product result = productService.create(product);

        verify(productRepository, times(1)).create(product);
        assertSame(product, result); // return object yang sama
    }

    @Test
    void findAllShouldReturnAllProductsFromIterator() {
        Product p1 = new Product();
        p1.setProductId("1");
        Product p2 = new Product();
        p2.setProductId("2");

        Iterator<Product> iterator = Arrays.asList(p1, p2).iterator();
        when(productRepository.findAll()).thenReturn(iterator);

        List<Product> result = productService.findAll();

        verify(productRepository, times(1)).findAll();
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getProductId());
        assertEquals("2", result.get(1).getProductId());
    }

    @Test
    void findByIdShouldCallRepositoryAndReturnProduct() {
        Product p = new Product();
        p.setProductId("99");
        when(productRepository.findById("99")).thenReturn(p);

        Product result = productService.findById("99");

        verify(productRepository, times(1)).findById("99");
        assertSame(p, result);
    }

    @Test
    void deleteShouldCallRepositoryAndReturnDeletedProduct() {
        Product p = new Product();
        p.setProductId("1");
        when(productRepository.delete(p)).thenReturn(p);

        Product result = productService.delete(p);

        verify(productRepository, times(1)).delete(p);
        assertSame(p, result);
    }

    @Test
    void updateShouldCallRepositoryAndReturnUpdatedProduct() {
        Product p = new Product();
        p.setProductId("1");
        when(productRepository.update(p)).thenReturn(p);

        Product result = productService.update(p);

        verify(productRepository, times(1)).update(p);
        assertSame(p, result);
    }
}