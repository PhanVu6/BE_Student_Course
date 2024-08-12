package com.example.taskmanagerstudent.configruation;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfig {

//    @Bean
//    @Primary
//    ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        return objectMapper;
//    }

//    @Bean
//    public MessageSource messageSource() {
//        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//        messageSource.setBasename("messages");
//        messageSource.setDefaultEncoding("UTF-8");
//        messageSource.getMessage("ST001", null, Locale.forLanguageTag("vi"));
//        return messageSource;
//    }
//
//    @Bean
//    public ReloadableResourceBundleMessageSource validationMessage() {
//        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//        messageSource.setBasename("validation");
//        messageSource.setDefaultEncoding("UTF-8");
//        // Load a key to trigger resource bundle loading
//        messageSource.getMessage("ST001", null, Locale.forLanguageTag("vi"));
//        return messageSource;
//    }
//
//    @Bean
//    @Primary
//    public LocalValidatorFactoryBean localValidatorFactoryBean() {
//        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
//        factory.setValidationMessageSource(validationMessage());
//        return factory;
//    }
}
