package com.chitova.florist.services.sync;

import com.chitova.florist.domain.product.Category;
import com.chitova.florist.outbound.products.response.ElasticPathHierarchyChildNodesResponse;
import com.chitova.florist.outbound.products.response.ElasticPathNodeChildrenResponse;

import java.util.ArrayList;
import java.util.List;

public final class CategoryMapper {

    public static Category toCategory(final ElasticPathNodeChildrenResponse.NodeData node, final String hierarchyId) {
        return Category.builder()
                .name(node.getAttributes().getName())
                .description(node.getAttributes().getDescription())
                .slug(node.getAttributes().getSlug())
                .elasticPathCloudCategoryId(node.getId())
                .elasticPathCloudHierarchyId(hierarchyId)
                .hasSubcategories(node.getMeta().isHasChildren())
                .subcategories(new ArrayList<>())
                .build();
    }

    public static Category toCategory(final ElasticPathHierarchyChildNodesResponse.DataItem node, final String hierarchyId) {
        return Category.builder()
                .name(node.getAttributes().getName())
                .description(node.getAttributes().getDescription())
                .slug(node.getAttributes().getSlug())
                .elasticPathCloudCategoryId(node.getId())
                .elasticPathCloudHierarchyId(hierarchyId)
                .hasSubcategories(node.getMeta().isHasChildren())
                .subcategories(new ArrayList<>())
                .build();
    }

    public static List<Category> mapNodeDataToCategories(final String hierarchyId, final ElasticPathNodeChildrenResponse node) {
        return node.getData()
                .stream()
                .map(data -> CategoryMapper.toCategory(data, hierarchyId))
                .toList();
    }
}