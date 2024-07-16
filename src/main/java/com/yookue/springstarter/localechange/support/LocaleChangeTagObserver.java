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

package com.yookue.springstarter.localechange.support;


import java.util.Collection;
import java.util.Locale;
import jakarta.annotation.Nullable;
import com.yookue.commonplexus.javaseutil.util.LocalePlainWraps;
import com.yookue.springstarter.localechange.property.LocaleChangeProperties;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@SuppressWarnings("unused")
public class LocaleChangeTagObserver {
    private final LocaleChangeProperties properties;

    public boolean contains(@Nullable String languageTag) {
        return properties.getModelAndView().getTagNames() != null && properties.getModelAndView().getTagNames().containsKey(languageTag);
    }

    public boolean contains(@Nullable Locale locale) {
        return locale != null && contains(locale.toLanguageTag());
    }

    public boolean equalsAny(@Nullable Locale locale, @Nullable Locale... comparisons) {
        return LocalePlainWraps.equalsAnyLanguageTags(locale, comparisons);
    }

    public boolean equalsAny(@Nullable Locale locale, @Nullable String... languageTags) {
        return LocalePlainWraps.equalsAnyLanguageTags(locale, languageTags);
    }

    public boolean equalsAny(@Nullable Locale locale, @Nullable Collection<Locale> comparisons) {
        return LocalePlainWraps.equalsAnyLanguageTags(locale, comparisons);
    }

    public String getLanguageName(@Nullable String languageTag) {
        return (properties.getModelAndView().getTagNames() == null) ? null : properties.getModelAndView().getTagNames().get(languageTag);
    }

    public String getLanguageName(@Nullable Locale locale) {
        return (locale == null) ? null : getLanguageName(locale.toLanguageTag());
    }
}
