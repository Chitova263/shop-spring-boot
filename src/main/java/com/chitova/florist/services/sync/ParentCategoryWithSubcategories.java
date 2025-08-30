package com.chitova.florist.services.sync;

import com.chitova.florist.domain.product.Category;

import java.util.List;

public record ParentCategoryWithSubcategories(String parentElasticPathCloudCategoryId, List<Category> subcategories) {
}
