package com.chitova.florist.controller;

import com.chitova.florist.model.configuration.GetConfigurationResponse;
import com.chitova.florist.services.ConfigurationService;
import com.chitova.florist.services.IdentityUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Configuration")
@RequestMapping("/florist-api")
@RestController
public class ConfigurationController {
    private final ConfigurationService configurationService;
    private final IdentityUserService identityUserService;

    public ConfigurationController(final ConfigurationService configurationService, final IdentityUserService identityUserService) {
        this.configurationService = configurationService;
        this.identityUserService = identityUserService;
    }

    @Operation(summary = "Get application configuration", description = "Returns application configuration")
    @GetMapping("/configuration")
    public GetConfigurationResponse  getConfiguration() {
        return configurationService.getConfiguration();
    }
}
