package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    void createProductPageShouldReturnCreateProductViewAndProvideProductModel() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void createProductPostShouldReturnCreateProductViewWhenValidationError() throws Exception {
        // productQuantity negatif -> melanggar @Min(0)
        mockMvc.perform(post("/product/create")
                        .param("productId", "1")
                        .param("productName", "Test")
                        .param("productQuantity", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"));

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
    void productListPageShouldReturnProductListViewAndProductsModel() throws Exception {
        Product p1 = new Product();
        p1.setProductId("1");
        Product p2 = new Product();
        p2.setProductId("2");

        when(service.findAll()).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attributeExists("products"));

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
    void editProductPageShouldReturnEditProductViewAndProductModel() throws Exception {
        Product p = new Product();
        p.setProductId("5");
        when(service.findById("5")).thenReturn(p);

        mockMvc.perform(get("/product/edit/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attributeExists("product"));

        verify(service, times(1)).findById("5");
    }

    @Test
    void editProductPostShouldReturnEditProductViewWhenValidationError() throws Exception {
        // sesuai controller kamu: return "EditProduct" (huruf E besar)
        mockMvc.perform(post("/product/edit")
                        .param("productId", "1")
                        .param("productName", "Test")
                        .param("productQuantity", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("EditProduct"));

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