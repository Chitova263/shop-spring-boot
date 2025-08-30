package com.chitova.florist.outbound.products.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public record ElasticPathProductsResponse(
        @JsonProperty("data") List<Product> data,
        @JsonProperty("included") IncludedResources included,
        @JsonProperty("meta") MetaInformation meta
) {
    public ElasticPathProductsResponse {
        data = data == null ? Collections.emptyList() : data;
    }

    public static record Product(
            @JsonProperty("id") String id,
            @JsonProperty("type") String type,
            @JsonProperty("attributes") ProductAttributes attributes,
            @JsonProperty("meta") ProductMeta meta,
            @JsonProperty("relationships") Relationships relationships
    ) {}

    public static record ProductAttributes(
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("slug") String slug,
            @JsonProperty("sku") String sku,
            @JsonProperty("status") String status,
            @JsonProperty("commodity_type") String commodityType,
            @JsonProperty("upc_ean") String upcEan,
            @JsonProperty("mpn") String mpn,
            @JsonProperty("external_ref") String externalRef,
            @JsonProperty("locales") Map<String, Object> locales,
            @JsonProperty("tags") List<String> tags,
            @JsonProperty("extensions") Extensions extensions,
            @JsonProperty("custom_inputs") Map<String, Object> customInputs,
            @JsonProperty("build_rules") BuildRules buildRules,
            @JsonProperty("components") Map<String, Object> components
    ) {
        public ProductAttributes {
            locales = locales == null ? Collections.emptyMap() : locales;
            tags = tags == null ? Collections.emptyList() : tags;
            customInputs = customInputs == null ? Collections.emptyMap() : customInputs;
            components = components == null ? Collections.emptyMap() : components;
        }
    }

    public static record Extensions(
            @JsonProperty("products(flower)") ProductFlower productFlower
    ) {}

    public static record ProductFlower(
            @JsonProperty("additional-information") String additionalInformation,
            @JsonProperty("information") String information
    ) {}

    public static record BuildRules(
            @JsonProperty("default") String defaultRule,
            @JsonProperty("include") List<List<String>> include,
            @JsonProperty("exclude") List<List<String>> exclude
    ) {
        public BuildRules {
            include = include == null ? Collections.emptyList() : include;
            exclude = exclude == null ? Collections.emptyList() : exclude;
        }
    }

    public static record ProductMeta(
            @JsonProperty("created_at") String createdAt,
            @JsonProperty("updated_at") String updatedAt,
            @JsonProperty("owner") String owner,
            @JsonProperty("variations") List<Variation> variations,
            @JsonProperty("custom_relationships") List<Object> customRelationships,
            @JsonProperty("child_variations") List<ChildVariation> childVariations,
            @JsonProperty("product_types") List<String> productTypes,
            @JsonProperty("variation_matrix") Map<String, Object> variationMatrix
    ) {
        public ProductMeta {
            variations = variations == null ? Collections.emptyList() : variations;
            customRelationships = customRelationships == null ? Collections.emptyList() : customRelationships;
            childVariations = childVariations == null ? Collections.emptyList() : childVariations;
            productTypes = productTypes == null ? Collections.emptyList() : productTypes;
            variationMatrix = variationMatrix == null ? Collections.emptyMap() : variationMatrix;
        }
    }

    public static record Variation(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("options") List<Option> options
    ) {
        public Variation {
            options = options == null ? Collections.emptyList() : options;
        }
    }

    public static record ChildVariation(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("sort_order") int sortOrder,
            @JsonProperty("options") List<Option> options,
            @JsonProperty("option") Option singleOption
    ) {
        public ChildVariation {
            options = options == null ? Collections.emptyList() : options;
        }
    }

    public static record Option(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("sort_order") Integer sortOrder
    ) {}

    public static record IncludedResources(
            @JsonProperty("main_images") List<FileResource> mainImages,
            @JsonProperty("component_products") List<Product> componentProducts,
            @JsonProperty("files") List<FileResource> files
    ) {
        public IncludedResources {
            mainImages = mainImages == null ? Collections.emptyList() : mainImages;
            componentProducts = componentProducts == null ? Collections.emptyList() : componentProducts;
            files = files == null ? Collections.emptyList() : files;
        }
    }

    public static record FileResource(
            @JsonProperty("id") String id,
            @JsonProperty("type") String type,
            @JsonProperty("file_name") String fileName,
            @JsonProperty("mime_type") String mimeType,
            @JsonProperty("file_size") int fileSize,
            @JsonProperty("public") boolean isPublic,
            @JsonProperty("meta") FileMeta meta,
            @JsonProperty("links") Link links,
            @JsonProperty("link") MetaLink link
    ) {}

    public static record FileMeta(
            @JsonProperty("timestamps") Map<String, String> timestamps,
            @JsonProperty("dimensions") Dimensions dimensions
    ) {
        public FileMeta {
            timestamps = timestamps == null ? Collections.emptyMap() : timestamps;
        }
    }

    public static record Dimensions(
            @JsonProperty("width") int width,
            @JsonProperty("height") int height
    ) {}

    public static record Link(@JsonProperty("self") String self) {}

    public static record MetaLink(
            @JsonProperty("href") String href,
            @JsonProperty("meta") Map<String, Object> meta
    ) {
        public MetaLink {
            meta = meta == null ? Collections.emptyMap() : meta;
        }
    }

    public static record MetaInformation(@JsonProperty("results") Results results) {}

    public static record Results(@JsonProperty("total") int total) {}

    public static record Relationships(
            @JsonProperty("base_product") Relationship baseProduct,
            @JsonProperty("children") Relationship children,
            @JsonProperty("component_products") Relationship componentProducts,
            @JsonProperty("custom_relationships") Relationship customRelationships,
            @JsonProperty("files") Relationship files,
            @JsonProperty("main_image") Relationship mainImage,
            @JsonProperty("templates") Relationship templates,
            @JsonProperty("variations") Relationship variations
    ) {}

    public static record Relationship(@JsonProperty("data") RelationshipData data) {}

    public static record RelationshipData(
            @JsonProperty("type") String type,
            @JsonProperty("id") String id
    ) {}
}
