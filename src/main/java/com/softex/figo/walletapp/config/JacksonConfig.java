package com.softex.figo.walletapp.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {
//    @Bean
//    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
//        return new Jackson2ObjectMapperBuilder()
//                .serializationInclusion(JsonInclude.Include.NON_NULL)
//                .featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
//                .failOnUnknownProperties(false)
//                .featuresToDisable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
//                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//                .indentOutput(true)
//                .modules(new JavaTimeModule());
//    }
//    @Bean
//    @Primary
//    public ObjectMapper objectMapper() {
//        return new ObjectMapper()
//                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
//                .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
//                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
//                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
//                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
//                .configure(SerializationFeature.INDENT_OUTPUT, true);
//    }
}
