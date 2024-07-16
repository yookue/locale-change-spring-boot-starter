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

package com.yookue.springstarter.localechange.dialect;


import jakarta.annotation.Nonnull;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import com.yookue.springstarter.localechange.factory.LocaleChangeExpressionFactory;
import lombok.Getter;
import lombok.Setter;


/**
 * {@link org.thymeleaf.dialect.IExpressionObjectDialect} for locale change
 *
 * @author David Hsing
 */
@Getter
@Setter
@SuppressWarnings("unused")
public class LocaleChangeThymeleafDialect extends AbstractDialect implements IExpressionObjectDialect {
    private static final String DIALECT_NAME = "Multilingual";    // $NON-NLS-1$
    private LocaleChangeExpressionFactory expressionFactory;

    public LocaleChangeThymeleafDialect() {
        super(DIALECT_NAME);
    }

    public LocaleChangeThymeleafDialect(@Nonnull LocaleChangeExpressionFactory factory) {
        super(DIALECT_NAME);
        expressionFactory = factory;
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return expressionFactory;
    }
}
