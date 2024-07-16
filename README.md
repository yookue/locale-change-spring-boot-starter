# Locale Change Spring Boot Starter

Spring Boot application enables an ability of switching multilingualism.

## Quickstart

- Import dependencies

```xml
    <dependency>
        <groupId>com.yookue.springstarter</groupId>
        <artifactId>locale-change-spring-boot-starter</artifactId>
        <version>LATEST</version>
    </dependency>
```

> By default, this starter will auto take effect, you can turn it off by `spring.locale-change.enabled = false`

- Configure Spring Boot `application.yml` with prefix `spring.locale-change`

```yml
spring:
    locale-change:
        model-and-view:
            view-attribute: 'localeChangeTagNames'
            tag-multilingual: false
            tag-names:
                'en-US': 'English'
                'zh-CN': '简体中文'
                'zh-TW': '繁体中文'
        locale-interceptor:
            param-name: 'lang'
            intercept-paths:
                - '/**'
            exclude-paths:
                - '/foo/**'
                - '/bar/**'
        cookie-locale-resolver:
            http-only: true
```

- **Optional feature**: If you want to display the `tag-names` value in multilingual (not the fixed value in `application.properties`), for example, display the value of `zh-CN` to `简体中文` when current request is using `zh` locale, or display `Simplified Chinese` when the request is using `en` locale. You can reach your goal as following:
  1. Configure the `tag-multilingual` attribute to `true`
  2. Set the value of `zh-CN` to `locale-change.dropdown-switch`
  3. Write the `locale-change.dropdown-switch` as a key in a resource bundle properties that could be loaded by the primary `MessageSource` bean (Just as our another Spring starter `message-source-spring-boot-starter` does)

- Write your template code as following (take `Thymeleaf` as an example)

Under the `head` segment (optional):

```html
<meta name="language-raw" th:content="${#locale.toLanguageTag()}" th:if="${not #localeChange.contains(#locale)}"/>
<meta name="language-tag" th:content="${#localeChange.contains(#locale) ? #locale.toLanguageTag() : 'en-US'}"/>
```

Under the `body` segment:

```html
<ul>
    <li class="lang-switch dropdown">
        <a href="javascript:">
            <i class="fa fa-globe" aria-hidden="true"></i>
            <span>Language</span>
            <i class="bi bi-chevron-down"></i>
        </a>
        <ul role="menu" th:if="${not #maps.isEmpty(localeChangeTagNames)}">
            <li th:each="tagName: ${localeChangeTagNames}"><a th:href="${'?lang=' + tagName.key}"><span th:text="${tagName.value}">Placeholder</span></a></li>
        </ul>
    </li>
</ul>
```

## Document

- Github: https://github.com/yookue/locale-change-spring-boot-starter

## Requirement

- jdk 1.8+

## License

This project is under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)

See the `NOTICE.txt` file for required notices and attributions.

## Donation

You like this package? Then [donate to Yookue](https://yookue.com/public/donate) to support the development.

## Website

- Yookue: https://yookue.com
