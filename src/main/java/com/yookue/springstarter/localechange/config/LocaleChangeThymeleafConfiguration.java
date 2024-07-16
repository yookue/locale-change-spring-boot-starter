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


import jakarta.annotation.Nonnull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.Thymeleaf;
import com.yookue.springstarter.localechange.dialect.LocaleChangeThymeleafDialect;
import com.yookue.springstarter.localechange.factory.LocaleChangeExpressionFactory;
import com.yookue.springstarter.localechange.property.LocaleChangeProperties;
import com.yookue.springstarter.localechange.support.LocaleChangeTagObserver;
import lombok.RequiredArgsConstructor;


/**
 * Configuration of {@link org.thymeleaf.dialect.IDialect} for locale change
 *
 * @author David Hsing
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = LocaleChangeViewConfiguration.PROPERTIES_PREFIX, name = {"enabled", "thymeleaf-dialect"}, havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(value = Thymeleaf.class)
@EnableConfigurationProperties(value = LocaleChangeProperties.class)
@RequiredArgsConstructor
@AutoConfigureAfter(value = ThymeleafAutoConfiguration.class)
public class LocaleChangeThymeleafConfiguration implements BeanPostProcessor {
    private final LocaleChangeProperties properties;

    @Override
    public Object postProcessAfterInitialization(@Nonnull Object bean, @Nonnull String beanName) throws BeansException {
        if (bean instanceof TemplateEngine instance) {
            LocaleChangeTagObserver observer = new LocaleChangeTagObserver(properties);
            LocaleChangeExpressionFactory factory = new LocaleChangeExpressionFactory(observer);
            LocaleChangeThymeleafDialect dialect = new LocaleChangeThymeleafDialect(factory);
            instance.addDialect(dialect);
        }
        return bean;
    }
}
