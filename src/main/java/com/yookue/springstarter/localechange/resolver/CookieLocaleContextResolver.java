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

package com.yookue.springstarter.localechange.resolver;


import java.util.Locale;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import com.yookue.commonplexus.javaseutil.constant.StringVariantConst;
import com.yookue.commonplexus.springutil.util.WebUtilsWraps;
import lombok.Getter;
import lombok.Setter;


/**
 * {@link org.springframework.web.servlet.LocaleContextResolver} for detecting locale from request parameters and cookies
 *
 * @author David Hsing
 * @see org.springframework.web.servlet.i18n.LocaleChangeInterceptor
 * @see "https://blog.csdn.net/flowingflying/article/details/76758577"
 */
@Getter
@Setter
@SuppressWarnings("unused")
public class CookieLocaleContextResolver extends CookieLocaleResolver {
    private final String cookieName;
    private String paramName = StringVariantConst.LANG;

    public CookieLocaleContextResolver(@Nonnull String cookieName) {
        super(cookieName);
        this.cookieName = cookieName;
    }

    @Nonnull
    @Override
    public Locale resolveLocale(@Nonnull HttpServletRequest request) {
        Locale locale = detectRequestLocale(request);
        return locale != null ? locale : super.resolveLocale(request);
    }

    @Nonnull
    @Override
    public LocaleContext resolveLocaleContext(@Nonnull HttpServletRequest request) {
        Locale locale = detectRequestLocale(request);
        return locale == null ? super.resolveLocaleContext(request) : new SimpleLocaleContext(locale);
    }

    @Nullable
    protected Locale detectRequestLocale(@Nonnull HttpServletRequest request) {
        Locale result = getLocaleFromParam(request);
        return result == null ? getLocaleFromCookie(request) : result;
    }

    @Nullable
    private Locale getLocaleFromParam(@Nonnull HttpServletRequest request) {
        String locale = request.getParameter(paramName);
        if (StringUtils.isBlank(locale)) {
            return null;
        }
        try {
            return super.parseLocaleValue(locale);
        } catch (Exception ignored) {
        }
        return null;
    }

    @Nullable
    private Locale getLocaleFromCookie(@Nonnull HttpServletRequest request) {
        return WebUtilsWraps.getLocaleFromCookie(request, cookieName);
    }
}
