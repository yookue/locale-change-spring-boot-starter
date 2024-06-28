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

package com.yookue.springstarter.localechange.factory;


import java.util.Collections;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;
import com.yookue.springstarter.localechange.support.LocaleChangeTagObserver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * {@link org.thymeleaf.expression.IExpressionObjectFactory} for locale change
 *
 * @author David Hsing
 */
@RequiredArgsConstructor
@Getter
public class LocaleChangeExpressionFactory implements IExpressionObjectFactory {
    private static final String EXPRESSION_NAME = "localeChange";    // $NON-NLS-1$
    private final LocaleChangeTagObserver observer;

    @Override
    public Object buildObject(@Nullable IExpressionContext context, @Nullable String expression) {
        return StringUtils.equals(expression, EXPRESSION_NAME) ? observer : null;
    }

    @Override
    public Set<String> getAllExpressionObjectNames() {
        return Collections.singleton(EXPRESSION_NAME);
    }

    @Override
    public boolean isCacheable(@Nullable String expression) {
        return StringUtils.equals(expression, EXPRESSION_NAME);
    }
}
