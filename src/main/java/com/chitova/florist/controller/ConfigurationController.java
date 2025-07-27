package com.chitova.florist.controller;

import com.chitova.florist.model.configuration.GetConfigurationResponse;
import com.chitova.florist.services.ConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Configuration", description = "Manage application configuration")
@RestController
public class ConfigurationController {
    private final ConfigurationService configurationService;

    public ConfigurationController(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Operation(summary = "Get application configuration", description = "Returns application configuration")
    @GetMapping(value = "/configuration", produces = {  MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<GetConfigurationResponse> getConfiguration() {
        return ResponseEntity.ok(configurationService.getConfiguration());
    }
}
