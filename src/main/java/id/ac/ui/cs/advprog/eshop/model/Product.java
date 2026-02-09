package id.ac.ui.cs.advprog.eshop.model;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Product {
    private String productId;
    private String productName;

    @Min(value = 0, message = "Quantity tidak boleh negatif")
    private int productQuantity;
}
