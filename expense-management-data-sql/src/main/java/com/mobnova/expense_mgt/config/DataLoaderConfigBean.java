package com.mobnova.expense_mgt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@PropertySource("classpath:custom.yaml")
@ConfigurationProperties(prefix = "dataloader")
public class DataLoaderConfigBean {

    private String baseModelPackage = "com.mobnova.expense_mgt.model";
    private String baseServicePackage = "com.mobnova.expense_mgt.services";
    private String bootstrapFilesFolder = "bootstrap";
}
