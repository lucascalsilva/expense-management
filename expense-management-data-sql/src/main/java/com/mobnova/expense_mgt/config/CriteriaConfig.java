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
@ConfigurationProperties(prefix = "criteria")
public class CriteriaConfig {

    private Integer minCharactersForLikeSearch = 3;
}
