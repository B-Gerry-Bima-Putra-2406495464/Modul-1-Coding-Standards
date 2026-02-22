package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Primary;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(ProductControllerTest.MockConfig.class)
@TestPropertySource(properties = "spring.thymeleaf.check-template-location=false")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService service;

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        ProductService productService() {
            return Mockito.mock(ProductService.class);
        }
    }

    @Test
    void createProductPageShouldReturn200() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk());
    }

    @Test
    void createProductPostShouldStayOnPageWhenBindingError() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("productId", "1")
                        .param("productName", "Test")
                        .param("productQuantity", "abc"))
                .andExpect(status().isOk());

        verify(service, never()).create(any(Product.class));
    }

    @Test
    void createProductPostShouldRedirectToListWhenValid() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("productId", "1")
                        .param("productName", "Test")
                        .param("productQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        verify(service, times(1)).create(any(Product.class));
    }

    @Test
    void productListPageShouldReturn200AndCallService() throws Exception {
        when(service.findAll()).thenReturn(Arrays.asList(new Product(), new Product()));

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk());

        verify(service, times(1)).findAll();
    }

    @Test
    void deleteProductShouldCallServiceAndRedirect() throws Exception {
        Product p = new Product();
        p.setProductId("99");
        when(service.findById("99")).thenReturn(p);

        mockMvc.perform(get("/product/delete/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("../list"));

        verify(service, times(1)).findById("99");
        verify(service, times(1)).delete(p);
    }

    @Test
    void editProductPageShouldReturn200() throws Exception {
        when(service.findById("5")).thenReturn(new Product());

        mockMvc.perform(get("/product/edit/5"))
                .andExpect(status().isOk());

        verify(service, times(1)).findById("5");
    }

    @Test
    void editProductPostShouldStayOnPageWhenBindingError() throws Exception {
        mockMvc.perform(post("/product/edit")
                        .param("productId", "1")
                        .param("productName", "Test")
                        .param("productQuantity", "abc"))
                .andExpect(status().isOk());

        verify(service, never()).update(any(Product.class));
    }

    @Test
    void editProductPostShouldRedirectToListWhenValid() throws Exception {
        mockMvc.perform(post("/product/edit")
                        .param("productId", "1")
                        .param("productName", "Updated")
                        .param("productQuantity", "20"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        verify(service, times(1)).update(any(Product.class));
    }
}