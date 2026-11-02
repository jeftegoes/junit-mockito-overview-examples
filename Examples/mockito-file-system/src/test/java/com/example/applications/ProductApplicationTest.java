package com.example.applications;

import com.example.entities.Product;
import com.example.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductApplicationTest {
    @InjectMocks
    ProductApplication productApplication;

    @Mock
    ProductService productService;

    @BeforeEach
    public void setup() {

    }

    @DisplayName("Should save image correctly.")
    @Test
    public void shouldSaveImageCorrectly() {
        when(productService.save(any(Product.class))).thenReturn(true);

        Product product = new Product(1, "A", 10, "X:/");

        productApplication.append(product);

        verify(productService, times(1)).save(any(Product.class));
    }

    @DisplayName("Should remove image correctly.")
    @Test
    public void shouldRemoveImageCorrectly() {
        productApplication.remove(1);

        verify(productService, times(1)).remove(any(Integer.class));
    }

    @DisplayName("Should update image correctly.")
    @Test
    public void shouldUpdateImageCorrectly() {
        Product product = new Product(1, "A", 10, "X:/");

        productApplication.update(1, product);

        verify(productService, times(1)).update(any(Product.class));
    }
}
