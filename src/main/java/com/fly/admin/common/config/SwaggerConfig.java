package com.fly.admin.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

import static com.fly.admin.common.constant.CommonConstant.TOKEN_HEADER;
import static java.util.Collections.singletonList;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/16
 */
@Configuration
public class SwaggerConfig {


    @Bean
    public Docket petApi() {

        List<SecurityScheme> securitySchemes = singletonList(new ApiKey(TOKEN_HEADER, TOKEN_HEADER, "header"));
        List<SecurityContext> securityContexts = singletonList(
                SecurityContext.builder()
                        .securityReferences(securityReferences())
                        .operationSelector(operationContext -> true)
                        .build()
        );

        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("fly admin api")
                .contact(new Contact("guoxiang", "null", "18841845137@163.com"))
                .version("1.0")
                .build();

        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo)
                .securitySchemes(securitySchemes)
                .securityContexts(securityContexts)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fly.admin"))
                .build();
    }

    public List<SecurityReference> securityReferences() {
        AuthorizationScope[] authorizationScopes =
                new AuthorizationScope[]{new AuthorizationScope("global", "accessEverything")};

        SecurityReference reference = new SecurityReference(TOKEN_HEADER, authorizationScopes);
        return singletonList(reference);
    }

}
