package com.diyiliu.support.config;

import com.diyiliu.support.format.CNLocalDateFormatter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.time.LocalDate;

/**
 * Description: WebMvcConfiguration
 * Author: DIYILIU
 * Update: 2017-10-25 08:57
 */

@Configuration
@SpringBootApplication
@EnableConfigurationProperties({PictureUploadProperties.class})
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(LocalDate.class, new CNLocalDateFormatter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }

/*    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer =
                new EmbeddedServletContainerCustomizer() {
                    @Override
                    public void customize(ConfigurableEmbeddedServletContainer container) {
                        container.addErrorPages(new ErrorPage(MultipartException.class, "/uploadError"));
                    }
                };

        return embeddedServletContainerCustomizer;
    }*/

    /**
     * lambda表达式
     * 处理MultipartException。它需要在Servlet 容器级别（也就是Tomcat 级别）
     * 来进行，因为这个异常不会由我们的控制器直接抛出。
     * 当MultipartException 出现的时候就会调用/uploadError页面
     * @return
     */
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return container ->
                container.addErrorPages(new ErrorPage(MultipartException.class, "/uploadError"));
    }
}
