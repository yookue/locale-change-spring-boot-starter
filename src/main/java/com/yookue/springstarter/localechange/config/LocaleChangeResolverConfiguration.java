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
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import com.yookue.commonplexus.javaseutil.util.CollectionPlainWraps;
import com.yookue.commonplexus.javaseutil.util.MapPlainWraps;
import com.yookue.commonplexus.javaseutil.util.StringUtilsWraps;
import com.yookue.commonplexus.springutil.constant.SpringBeanConst;
import com.yookue.springstarter.localechange.filter.CookieLocaleRequestContextFilter;
import com.yookue.springstarter.localechange.property.LocaleChangeProperties;
import com.yookue.springstarter.localechange.resolver.CookieLocaleContextResolver;


/**
 * Configuration of {@link org.springframework.web.servlet.LocaleContextResolver} for locale change
 *
 * @author David Hsing
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = LocaleChangeViewConfiguration.PROPERTIES_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfigureAfter(value = DispatcherServletAutoConfiguration.class)
@AutoConfigureBefore(value = WebMvcAutoConfiguration.class)
@AutoConfigureOrder(value = Ordered.HIGHEST_PRECEDENCE + 8)
@Import(value = {LocaleChangeResolverConfiguration.Entry.class, LocaleChangeResolverConfiguration.Cookie.class, LocaleChangeResolverConfiguration.Session.class, LocaleChangeResolverConfiguration.Fixed.class})
public class LocaleChangeResolverConfiguration {
    @Order(value = 0)
    @EnableConfigurationProperties(value = LocaleChangeProperties.class)
    static class Entry {
    }


    @Order(value = 1)
    @ConditionalOnProperty(prefix = LocaleChangeViewConfiguration.PROPERTIES_PREFIX, name = "locale-resolver-type", havingValue = "cookie", matchIfMissing = true)
    static class Cookie {
        /**
         * @see org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#requestContextFilter
         */
        @Bean
        @ConditionalOnMissingBean(name = SpringBeanConst.REQUEST_CONTEXT_FILTER)
        public FilterRegistrationBean<RequestContextFilter> requestContextFilterRegistration(@Nonnull LocaleChangeProperties properties) {
            CookieLocaleRequestContextFilter filter = new CookieLocaleRequestContextFilter(properties);
            LocaleChangeProperties.CookieLocaleFilter filterProps = properties.getCookieLocaleFilter();
            LocaleChangeProperties.CookieLocaleResolver resolverProps = properties.getCookieLocaleResolver();
            filter.setThreadLocaleInheritable(BooleanUtils.isTrue(filterProps.getThreadInheritable()));
            StringUtilsWraps.ifNotBlank(resolverProps.getCookieName(), filter::setCookieName);
            FilterRegistrationBean<RequestContextFilter> result = new FilterRegistrationBean<>(filter);
            result.setName(SpringBeanConst.REQUEST_CONTEXT_FILTER);
            Optional.ofNullable(filterProps.getFilerOrder()).ifPresent(result::setOrder);
            CollectionPlainWraps.ifNotEmpty(filterProps.getFilterPaths(), result::setUrlPatterns);
            MapPlainWraps.ifNotEmpty(filterProps.getFilterParams(), result::setInitParameters);
            return result;
        }

        /**
         * @see org.springframework.web.servlet.i18n.CookieLocaleResolver
         */
        @Bean(name = DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME)
        @ConditionalOnProperty(prefix = LocaleChangeViewConfiguration.PROPERTIES_PREFIX + ".cookie-locale-resolver", name = "cookie-name")
        @ConditionalOnMissingBean(name = DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME)
        public LocaleContextResolver localeContextResolver(@Nonnull LocaleChangeProperties properties) {
            CookieLocaleContextResolver resolver = new CookieLocaleContextResolver(properties.getCookieLocaleResolver().getCookieName());
            resolver.setParamName(properties.getLocaleInterceptor().getParamName());
            LocaleChangeProperties.CookieLocaleResolver props = properties.getCookieLocaleResolver();
            StringUtilsWraps.ifNotBlank(props.getCookiePath(), resolver::setCookiePath);
            StringUtilsWraps.ifNotBlank(props.getCookieDomain(), resolver::setCookieDomain);
            Optional.ofNullable(props.getMaxAge()).ifPresent(resolver::setCookieMaxAge);
            resolver.setCookieHttpOnly(BooleanUtils.isTrue(props.getHttpOnly()));
            resolver.setCookieSecure(BooleanUtils.isTrue(props.getSecureProtocol()));
            resolver.setRejectInvalidCookies(BooleanUtils.isTrue(props.getRejectInvalid()));
            Optional.ofNullable(props.getDefaultLocale()).ifPresent(resolver::setDefaultLocale);
            Optional.ofNullable(props.getDefaultTimeZone()).ifPresent(resolver::setDefaultTimeZone);
            return resolver;
        }
    }


    @Order(value = 2)
    @ConditionalOnProperty(prefix = LocaleChangeViewConfiguration.PROPERTIES_PREFIX, name = "locale-resolver-type", havingValue = "session")
    static class Session {
        /**
         * @see org.springframework.web.servlet.i18n.SessionLocaleResolver
         */
        @Bean(name = DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME)
        @ConditionalOnMissingBean(name = DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME)
        public LocaleContextResolver localeContextResolver(@Nonnull LocaleChangeProperties properties) {
            SessionLocaleResolver resolver = new SessionLocaleResolver();
            LocaleChangeProperties.SessionLocaleResolver props = properties.getSessionLocaleResolver();
            StringUtilsWraps.ifNotBlank(props.getSessionName(), resolver::setLocaleAttributeName);
            StringUtilsWraps.ifNotBlank(props.getTimezoneName(), resolver::setTimeZoneAttributeName);
            Optional.ofNullable(props.getDefaultLocale()).ifPresent(resolver::setDefaultLocale);
            Optional.ofNullable(props.getDefaultTimeZone()).ifPresent(resolver::setDefaultTimeZone);
            return resolver;
        }
    }


    @Order(value = 3)
    @ConditionalOnProperty(prefix = LocaleChangeViewConfiguration.PROPERTIES_PREFIX, name = "locale-resolver-type", havingValue = "fixed")
    static class Fixed {
        /**
         * @see org.springframework.web.servlet.i18n.FixedLocaleResolver
         */
        @Bean(name = DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME)
        @ConditionalOnMissingBean(name = DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME)
        public LocaleContextResolver localeContextResolver(@Nonnull LocaleChangeProperties properties) {
            FixedLocaleResolver resolver = new FixedLocaleResolver();
            LocaleChangeProperties.SessionLocaleResolver props = properties.getSessionLocaleResolver();
            Optional.ofNullable(props.getDefaultLocale()).ifPresent(resolver::setDefaultLocale);
            Optional.ofNullable(props.getDefaultTimeZone()).ifPresent(resolver::setDefaultTimeZone);
            return resolver;
        }
    }
}
