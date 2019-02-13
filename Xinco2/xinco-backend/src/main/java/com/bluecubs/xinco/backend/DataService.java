package com.bluecubs.xinco.backend;

import java.io.Serializable;
import java.util.Collection;

import com.bluecubs.xinco.backend.data.Category;
import com.bluecubs.xinco.backend.data.Product;
import com.bluecubs.xinco.backend.mock.MockDataService;

/**
 * Back-end service interface for retrieving and updating product data.
 */
public abstract class DataService implements Serializable {

    public abstract Collection<Product> getAllProducts();

    public abstract Collection<Category> getAllCategories();

    public abstract void updateProduct(Product p);

    public abstract void deleteProduct(int productId);

    public abstract Product getProductById(int productId);

    public static DataService get() {
        return MockDataService.getInstance();
    }

}
