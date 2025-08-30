package com.chitova.florist.fixture;

import com.chitova.florist.domain.product.Category;
import com.chitova.florist.domain.product.Product;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ProductResponseFixture {

    public static final Product PRODUCT1 = Product.builder()
            .id(new ObjectId("68c28a17efa09ea7f7dc6c84"))
            .name("Product1")
            .sku("PRODUCT_ONE_SKU")
            .information("Information")
            .additionalInformation("Additional Information")
            .bestseller(true)
            .elasticPathCloudProductId("Elastic_Path_Cloud_Id")
            .isChildProduct(false)
            .build();
    public static final Product PRODUCT2 = Product.builder()
            .id(new ObjectId("68c28a17efa09ea7f7dc6c81"))
            .name("Product1")
            .sku("PRODUCT_ONE_SKU")
            .information("Information")
            .additionalInformation("Additional Information")
            .bestseller(true)
            .elasticPathCloudProductId("Elastic_Path_Cloud_Id")
            .isChildProduct(false)
            .build();
    public static final Product PRODUCT3 = Product.builder()
            .id(new ObjectId("68c28a17efa09ea7f7dc6c83"))
            .name("Product2")
            .sku("PRODUCT_2_SKU")
            .information("Product_2_Information")
            .additionalInformation("Product_2 Additional Information")
            .bestseller(true)
            .elasticPathCloudProductId("Product_2 Elastic_Path_Cloud_Id")
            .isChildProduct(false)
            .build();
    public static final Product PRODUCT4 = Product.builder()
            .id(new ObjectId("61c28a17efa09ea7f7dc6c84"))
            .name("Product3")
            .sku("PRODUCT_3_SKU")
            .information("Product_3_Information")
            .additionalInformation("Product_3 Additional Information")
            .bestseller(true)
            .elasticPathCloudProductId("Product_3 Elastic_Path_Cloud_Id")
            .isChildProduct(false)
            .build();

    public static final List<Product> PRODUCT_LIST = List.of(
            PRODUCT1,
            PRODUCT2,
            PRODUCT3,
            PRODUCT4
    );

    public static List<Product> getAllProducts() {
        return PRODUCT_LIST;
    }

    public static List<Category> getAllCategories() {
        return List.of(
                Category.builder()
                        .id(new ObjectId(""))
                        .name("Category1")
                        .description("Category1 description")
                        .elasticPathCloudCategoryId("ElasticPathCategoryId1")
                        .slug("category1_slug")
                        .hasSubcategories(true)
                        .elasticPathCloudHierarchyId("ElasticPathHierarchyId")
                        .subcategories(List.of(
                                Category.builder()
                                        .id(new ObjectId(""))
                                        .name("SubCategory1")
                                        .description("SubCategory1 description")
                                        .elasticPathCloudCategoryId("ElasticPathCategoryId1")
                                        .slug("subcategory1_slug")
                                        .hasSubcategories(true)
                                        .elasticPathCloudHierarchyId("ElasticPathHierarchyId")
                                        .subcategories(new ArrayList<>())
                                        .build()
                        ))
                        .build()
        );
    }
}
