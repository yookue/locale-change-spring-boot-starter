/*
 * Copyright (c) 2022 Yookue Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yookue.springstarter.localechange.config;


import java.util.Optional;
import jakarta.annotation.Nonnull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.yookue.commonplexus.javaseutil.constant.AssertMessageConst;
import com.yookue.commonplexus.javaseutil.util.CollectionPlainWraps;
import com.yookue.springstarter.localechange.interceptor.LocaleChangeViewInterceptor;
import com.yookue.springstarter.localechange.property.LocaleChangeProperties;
import lombok.RequiredArgsConstructor;


/**
 * Configuration of view interceptor for locale change
 *
 * @author David Hsing
 */
@Configuration
@ConditionalOnProperty(prefix = LocaleChangeViewConfiguration.PROPERTIES_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(value = LocaleChangeProperties.class)
@RequiredArgsConstructor
public class LocaleChangeViewConfiguration implements WebMvcConfigurer {
    public static final String PROPERTIES_PREFIX = "spring.locale-change";    // $NON-NLS-1$
    public static final String VIEW_INTERCEPTOR = "localeChangeViewInterceptor";    // $NON-NLS-1$
    private final LocaleChangeProperties properties;

    @Bean(name = VIEW_INTERCEPTOR)
    @ConditionalOnMissingBean
    public LocaleChangeViewInterceptor viewInterceptor() {
        return new LocaleChangeViewInterceptor(properties);
    }

    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        LocaleChangeProperties.ModelAndView props = properties.getModelAndView();
        Assert.notEmpty(props.getInterceptPaths(), AssertMessageConst.NOT_EMPTY);
        InterceptorRegistration registration = registry.addInterceptor(viewInterceptor()).addPathPatterns(props.getInterceptPaths());
        Optional.ofNullable(props.getInterceptorOrder()).ifPresent(registration::order);
        CollectionPlainWraps.ifNotEmpty(props.getExcludePaths(), element -> registration.excludePathPatterns(element));
    }
}
