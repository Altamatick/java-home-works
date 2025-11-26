package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
    }

    @Test
    void constructor_ShouldInitializeWithDefaultValues() {
        Product newProduct = new Product();

        assertThat(newProduct.getId()).isNull();
        assertThat(newProduct.getName()).isNull();
        assertThat(newProduct.getPrice()).isNull();
    }

    @Test
    void constructor_ShouldInitializeWithNameAndPrice() {
        Product newProduct = new Product("Laptop", 1200.0);

        assertThat(newProduct.getName()).isEqualTo("Laptop");
        assertThat(newProduct.getPrice()).isEqualTo(1200.0);
        assertThat(newProduct.getId()).isNull();
    }

    @Test
    void constructor_ShouldInitializeWithAllParameters() {
        Product newProduct = new Product(1L, "Laptop", 1200.0);

        assertThat(newProduct.getId()).isEqualTo(1L);
        assertThat(newProduct.getName()).isEqualTo("Laptop");
        assertThat(newProduct.getPrice()).isEqualTo(1200.0);
    }

    @Test
    void setId_ShouldSetId() {
        product.setId(1L);

        assertThat(product.getId()).isEqualTo(1L);
    }

    @Test
    void setName_ShouldSetName() {
        product.setName("Mouse");

        assertThat(product.getName()).isEqualTo("Mouse");
    }

    @Test
    void setPrice_ShouldSetPrice() {
        product.setPrice(25.0);

        assertThat(product.getPrice()).isEqualTo(25.0);
    }

    @Test
    void equals_ShouldReturnTrue_WhenProductsHaveSameId() {
        product.setId(1L);
        Product otherProduct = new Product();
        otherProduct.setId(1L);

        assertThat(product).isEqualTo(otherProduct);
    }

    @Test
    void equals_ShouldReturnFalse_WhenProductsHaveDifferentId() {
        product.setId(1L);
        Product otherProduct = new Product();
        otherProduct.setId(2L);

        assertThat(product).isNotEqualTo(otherProduct);
    }

    @Test
    void equals_ShouldReturnTrue_WhenComparingWithItself() {
        assertThat(product).isEqualTo(product);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparingWithNull() {
        assertThat(product.equals(null)).isFalse();
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparingWithDifferentClass() {
        assertThat(product.equals(new String("test"))).isFalse();
    }

    @Test
    void hashCode_ShouldReturnSameValue_ForProductsWithSameId() {
        product.setId(1L);
        Product otherProduct = new Product();
        otherProduct.setId(1L);

        assertThat(product.hashCode()).isEqualTo(otherProduct.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectFormat() {
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(1200.0);

        String result = product.toString();

        assertThat(result).contains("id=1");
        assertThat(result).contains("name='Laptop'");
        assertThat(result).contains("price=1200.0");
    }

    @Test
    void toString_ShouldHandleNullValues() {
        String result = product.toString();

        assertThat(result).contains("id=null");
        assertThat(result).contains("name='null'");
        assertThat(result).contains("price=null");
    }
}

