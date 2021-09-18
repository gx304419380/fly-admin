package com.fly.admin.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.admin.common.dto.Res;
import com.fly.admin.common.dto.UserInfo;
import com.fly.admin.common.util.CacheUtils;
import com.fly.admin.common.util.Check;
import com.fly.admin.common.util.UserUtils;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.fly.admin.common.constant.CommonConstant.TOKEN;
import static com.fly.admin.common.constant.CommonConstant.TOKEN_CACHE;
import static com.fly.admin.common.dto.Res.USER_LOGOUT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/16
 */
@WebFilter(urlPatterns = "/*", filterName = "tokenFilter")
public class TokenFilter implements Filter {

    private static final String SWAGGER = "/swagger";
    private static final String SWAGGER_JSON = "/v3/api-docs";
    private static final String ACCOUNT = "/account";

    @Resource
    private ObjectMapper objectMapper;

    /**
     * token校验
     *
     * @param request       request
     * @param response      response
     * @param chain         chain
     * @throws IOException  IOException
     * @throws ServletException ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        //白名单
        String servletPath = httpRequest.getServletPath();
        if (whiteList(servletPath)) {
            chain.doFilter(request, response);
            return;
        }

        //校验token
        String token = httpRequest.getHeader(TOKEN);
        if (Check.isEmpty(token)) {
            setResponse(httpResponse, "请求头无token");
            return;
        }

        //校验用户信息
        UserInfo user = CacheUtils.get(TOKEN_CACHE + token);
        if (user == null) {
            setResponse(httpResponse, "token已失效");
            return;
        }

        //将数据放到thread local
        try {
            UserUtils.put(user);
            chain.doFilter(request, response);
        } finally {
            UserUtils.remove();
        }

    }

    /**
     * 白名单校验
     *
     * @param uri   url
     * @return      是否为白名单
     */
    private boolean whiteList(String uri) {
        return uri.startsWith(SWAGGER)
                || uri.startsWith(SWAGGER_JSON)
                || uri.startsWith(ACCOUNT);
    }


    /**
     * 返回校验信息
     *
     * @param response  response
     * @param message   message
     * @throws IOException  异常
     */
    private void setResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());

        Res<Object> res = Res.fail(USER_LOGOUT, message);
        String json = objectMapper.writeValueAsString(res);

        response.getWriter().write(json);
    }
}
