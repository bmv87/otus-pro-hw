package ru.otus.pro.hw.serialization.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import ru.otus.pro.hw.serialization.CustomLocalDateTimeDeserializer;
import ru.otus.pro.hw.serialization.CustomLocalDateTimeSerializer;
import ru.otus.pro.hw.serialization.CustomOffsetDateTimeDeserializer;
import ru.otus.pro.hw.serialization.SmsStatusDeserializer;
import ru.otus.pro.hw.serialization.models.in.SmsStatus;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;

@Configuration
public class ModuleConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {

        var localDateTimeSerializer = new CustomLocalDateTimeSerializer();
        var localDateTimeDeserializer = new CustomLocalDateTimeDeserializer();
        var offsetDateTimeDeserializer = new CustomOffsetDateTimeDeserializer();
        var smsStatusDeserializer = new SmsStatusDeserializer();

        return builder -> builder.serializationInclusion(JsonInclude.Include.NON_NULL)
                .indentOutput(true)
                .serializersByType(new HashMap<>() {{
                    put(LocalDateTime.class, localDateTimeSerializer);
                }})
                .deserializersByType(new HashMap<>() {{
                    put(LocalDateTime.class, localDateTimeDeserializer);
                }})
                .deserializersByType(new HashMap<>() {{
                    put(OffsetDateTime.class, offsetDateTimeDeserializer);
                }})
                .deserializersByType(new HashMap<>() {{
                    put(SmsStatus.class, smsStatusDeserializer);
                }});

    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(Jackson2ObjectMapperBuilder builder) {
        return new MappingJackson2HttpMessageConverter(builder.build());
    }

    @Bean
    public MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter(Jackson2ObjectMapperBuilder builder) {
        var mapper = builder.createXmlMapper(true).build();
        return new MappingJackson2XmlHttpMessageConverter(mapper);
    }
}
