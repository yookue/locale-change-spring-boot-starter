/*
 * Copyright (c) 2016 Yookue Ltd. All rights reserved.
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
import javax.annotation.Nonnull;
import org.apache.commons.lang3.BooleanUtils;
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
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import com.yookue.commonplexus.javaseutil.constant.AssertMessageConst;
import com.yookue.commonplexus.javaseutil.util.CollectionPlainWraps;
import com.yookue.commonplexus.javaseutil.util.StringUtilsWraps;
import com.yookue.springstarter.localechange.property.LocaleChangeProperties;
import lombok.RequiredArgsConstructor;


/**
 * Configuration of {@link org.springframework.web.servlet.i18n.LocaleChangeInterceptor} for locale change
 *
 * @author David Hsing
 */
@Configuration
@ConditionalOnProperty(prefix = LocaleChangeViewConfiguration.PROPERTIES_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(value = LocaleChangeProperties.class)
@RequiredArgsConstructor
public class LocaleChangeInterceptorConfiguration implements WebMvcConfigurer {
    private final LocaleChangeProperties properties;

    /**
     * Usage:
     * <pre><code>
     *     &lt;a href="?lang=en-US"&gt;English&lt;/a&gt;
     *     &lt;a href="?lang=en_US"&gt;English&lt;/a&gt;
     * </code></pre>
     *
     * @reference "http://guanxi.iteye.com/blog/2304607"
     */
    @Bean
    @ConditionalOnMissingBean
    @SuppressWarnings({"JavadocDeclaration", "JavadocLinkAsPlainText"})
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        LocaleChangeProperties.LocaleInterceptor props = properties.getLocaleInterceptor();
        StringUtilsWraps.ifNotBlank(props.getParamName(), interceptor::setParamName);
        interceptor.setIgnoreInvalidLocale(BooleanUtils.isTrue(props.getIgnoreInvalidLocale()));
        return interceptor;
    }

    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        LocaleChangeProperties.LocaleInterceptor props = properties.getLocaleInterceptor();
        Assert.notEmpty(props.getInterceptPaths(), AssertMessageConst.NOT_EMPTY);
        InterceptorRegistration registration = registry.addInterceptor(localeChangeInterceptor()).addPathPatterns(props.getInterceptPaths());
        Optional.ofNullable(props.getInterceptorOrder()).ifPresent(registration::order);
        CollectionPlainWraps.ifNotEmpty(props.getExcludePaths(), element -> registration.excludePathPatterns(element));
    }
}
