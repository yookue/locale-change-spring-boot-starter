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

package com.yookue.springstarter.localechange.property;


import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.springframework.boot.context.properties.ConfigurationProperties;
import com.yookue.commonplexus.javaseutil.constant.StringVariantConst;
import com.yookue.commonplexus.springutil.constant.AntPathConst;
import com.yookue.commonplexus.springutil.constant.SpringAttributeConst;
import com.yookue.springstarter.localechange.config.LocaleChangeViewConfiguration;
import com.yookue.springstarter.localechange.enumeration.LocaleResolverType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Properties for locale change
 *
 * @author David Hsing
 * @reference "https://www.techonthenet.com/js/language_tags.php"
 */
@ConfigurationProperties(prefix = LocaleChangeViewConfiguration.PROPERTIES_PREFIX)
@Getter
@Setter
@ToString
@SuppressWarnings({"JavadocDeclaration", "JavadocLinkAsPlainText"})
public class LocaleChangeProperties implements Serializable {
    /**
     * Indicates whether to enable this starter or not
     * <p>
     * Default is {@code true}
     */
    private Boolean enabled = true;

    /**
     * Model and view attributes
     */
    private final ModelAndView modelAndView = new ModelAndView();

    /**
     * Locale change interceptor attributes
     */
    private final LocaleInterceptor localeInterceptor = new LocaleInterceptor();

    /**
     * The type of locale resolver
     * <p>
     * Default is {@code COOKIE}
     */
    private LocaleResolverType localeResolverType = LocaleResolverType.COOKIE;

    /**
     * Cookie based locale filter attributes
     */
    private final CookieLocaleFilter cookieLocaleFilter = new CookieLocaleFilter();

    /**
     * Cookie based locale resolver attributes
     */
    private final CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();

    /**
     * Session based locale resolver attributes
     */
    private final SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();

    /**
     * Fixed locale resolver attributes
     */
    private final FixedLocaleResolver fixedLocaleResolver = new FixedLocaleResolver();


    /**
     * Properties for model and view
     *
     * @author David Hsing
     * @see org.springframework.web.servlet.ModelAndView
     * @see com.yookue.springstarter.localechange.interceptor.LocaleChangeViewInterceptor
     */
    @Getter
    @Setter
    @ToString
    public static class ModelAndView implements Serializable {
        /**
         * The attribute name to add to the view
         * <p>
         * The attribute value is {@code tagNames}
         */
        private String viewAttribute = "localeChangeTagNames";    // $NON-NLS-1$

        /**
         * Indicates whether to use message resource to display the tag name or not
         */
        private Boolean tagMultilingual;

        /**
         * The locale-tag and locale-name mappings
         */
        private Map<String, String> tagNames = new LinkedHashMap<>();

        /**
         * The priority order of the interceptor
         */
        private Integer interceptorOrder;

        /**
         * The path patterns to be intercepted
         */
        private List<String> interceptPaths = Collections.singletonList(AntPathConst.SLASH_STARS);

        /**
         * The path patterns to be excluded
         */
        private List<String> excludePaths;

        /**
         * Enable thymeleaf dialect, default is {@code true}
         */
        private Boolean thymeleafDialect;
    }


    /**
     * Properties for locale change interceptor
     *
     * @author David Hsing
     * @see org.springframework.web.servlet.i18n.LocaleChangeInterceptor
     */
    @Getter
    @Setter
    @ToString
    public static class LocaleInterceptor implements Serializable {
        /**
         * Param name of the locale specification parameter
         */
        private String paramName = StringVariantConst.LANG;

        /**
         * Indicates whether ignore invalid locale or not
         * <p>
         * Default is {@code true}
         */
        private Boolean ignoreInvalidLocale = true;

        /**
         * The priority order of the interceptor
         */
        private Integer interceptorOrder;

        /**
         * The path patterns to be intercepted
         */
        private List<String> interceptPaths = Collections.singletonList(AntPathConst.SLASH_STARS);

        /**
         * The path patterns to be excluded
         */
        private List<String> excludePaths;
    }


    /**
     * Properties for cookie locale filter
     *
     * @author David Hsing
     * @see com.yookue.springstarter.localechange.filter.CookieLocaleRequestContextFilter
     */
    @Getter
    @Setter
    @ToString
    public static class CookieLocaleFilter implements Serializable {
        /**
         * The priority order of the filter
         */
        private Integer filerOrder;

        /**
         * The url patterns of the filter
         */
        private Set<String> filterPaths;

        /**
         * The init parameters of the filter
         */
        private Map<String, String> filterParams;

        /**
         * The url patterns that ignored by the filter
         */
        private Set<String> excludePaths;

        /**
         * Indicates whether thread inheritable or not
         * <p>
         * Default is {@code true}
         */
        private Boolean threadInheritable = true;
    }


    /**
     * Properties for cookie locale resolver
     *
     * @author David Hsing
     */
    @Getter
    @Setter
    @ToString
    public static class CookieLocaleResolver implements Serializable {
        /**
         * Specifies a name for the cookie
         */
        private String cookieName = SpringAttributeConst.LOCALE_RESOLVER_LOCALE;

        /**
         * Specifies a path for the cookie
         */
        private String cookiePath = org.springframework.web.servlet.i18n.CookieLocaleResolver.DEFAULT_COOKIE_PATH;

        /**
         * Specifies the domain name of cookie
         * <p>
         * Domain names are formatted according to RFC 2109
         */
        private String cookieDomain;

        /**
         * Indicates whether the cookie is supposed to be marked with the "HttpOnly" attribute or not
         */
        private Boolean httpOnly = false;

        /**
         * Specifies the maximum age of the cookie in seconds
         * <p>
         * If negative, means the cookie is not stored; if zero, deletes the cookie
         */
        private Integer maxAge;

        /**
         * Indicates whether the cookie should only be sent using a secure protocol, such as HTTPS (SSL)
         */
        private Boolean secureProtocol = false;

        /**
         * Indicates whether this resolver's cookies should be compliant with BCP-47 language tags instead of Java's legacy locale specification format or not
         * <p>
         * Default is {@code true}
         */
        private Boolean bcp47Compliant = true;

        /**
         * Indicates whether to reject cookies with invalid content (e.g. invalid format) or not
         * <p>
         * Default is {@code true}
         */
        private Boolean rejectInvalid = true;

        /**
         * Set a fixed locale that this resolver will return if no cookie is found
         */
        private Locale defaultLocale;

        /**
         * Set a fixed time zone that this resolver will return if no cookie is found
         */
        private TimeZone defaultTimeZone;
    }


    /**
     * Properties for session locale resolver
     *
     * @author David Hsing
     */
    @Getter
    @Setter
    @ToString
    public static class SessionLocaleResolver implements Serializable {
        /**
         * Specifies a name for the locale attribute
         */
        private String sessionName = SpringAttributeConst.LOCALE_RESOLVER_LOCALE;

        /**
         * Specifies a path for the cookie
         */
        private String timezoneName = SpringAttributeConst.LOCALE_RESOLVER_TIMEZONE;

        /**
         * Set a fixed locale that this resolver will return if no cookie is found
         */
        private Locale defaultLocale;

        /**
         * Set a fixed time zone that this resolver will return if no cookie is found
         */
        private TimeZone defaultTimeZone;
    }


    /**
     * Properties for fixed locale resolver
     *
     * @author David Hsing
     */
    @Getter
    @Setter
    @ToString
    public static class FixedLocaleResolver implements Serializable {
        /**
         * Set a fixed locale that this resolver will return if no cookie is found
         */
        private Locale defaultLocale;

        /**
         * Set a fixed time zone that this resolver will return if no cookie is found
         */
        private TimeZone defaultTimeZone;
    }
}
