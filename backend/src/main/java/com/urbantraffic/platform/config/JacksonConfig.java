package com.urbantraffic.platform.config;

import tools.jackson.datatype.hibernate7.Hibernate7Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Hibernate7Module hibernate7Module() {
        Hibernate7Module module = new Hibernate7Module();
        module.enable(Hibernate7Module.Feature.FORCE_LAZY_LOADING);
        return module;
    }
}