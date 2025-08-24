package com.chitova.florist.services.sync;

import com.chitova.florist.entities.product.Category;
import com.chitova.florist.outbound.products.response.ElasticPathHierarchyChildNodesResponse;
import com.chitova.florist.outbound.products.response.ElasticPathNodeChildrenResponse;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public final class CategoryMapper {

    public static Category toCategory(final ElasticPathNodeChildrenResponse.NodeData node, final String hierarchyId) {
        return Category.builder()
                .id(new ObjectId())
                .name(node.getAttributes().getName())
                .description(node.getAttributes().getDescription())
                .slug(node.getAttributes().getSlug())
                .elasticPathCloudCategoryId(node.getId())
                .elasticPathCloudHierarchyId(hierarchyId)
                .hasChildren(node.getMeta().isHasChildren())
                .subcategories(new ArrayList<>())
                .build();
    }

    public static Category toCategory(final ElasticPathHierarchyChildNodesResponse.DataItem node, final String hierarchyId) {
        return Category.builder()
                .id(new ObjectId())
                .name(node.getAttributes().getName())
                .description(node.getAttributes().getDescription())
                .slug(node.getAttributes().getSlug())
                .elasticPathCloudCategoryId(node.getId())
                .elasticPathCloudHierarchyId(hierarchyId)
                .hasChildren(node.getMeta().isHasChildren())
                .subcategories(new ArrayList<>())
                .build();
    }
}