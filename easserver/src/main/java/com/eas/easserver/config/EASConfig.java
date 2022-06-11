package com.eas.easserver.config;

import com.eas.easserver.interceptor.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class EASConfig implements WebMvcConfigurer {
    @Autowired
    private RequestInterceptor interceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods(new String[]{"GET","POST","PUT","DELETE"})
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}
