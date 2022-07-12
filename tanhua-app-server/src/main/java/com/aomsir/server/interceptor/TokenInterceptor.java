package com.aomsir.server.interceptor;

import com.aomsir.commons.utils.JwtUtils;
import com.aomsir.model.domain.User;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Aomsir
 * @Date: 2022/7/12
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 1.获取请求头
        String token = request.getHeader("Authorization");

        // 2.使用工具类判断token是否有效
        boolean verifyToken = JwtUtils.verifyToken(token);

        // 3.token失效则返回401状态码，拦截
        if (!verifyToken) {
            response.setStatus(401);
            return false;
        }

        // 4.token正常可用，放行
        Claims claims = JwtUtils.getClaims(token);
        String mobile = (String) claims.get("mobile");
        Integer id = (Integer) claims.get("id");

        User user = new User();
        user.setId(Long.valueOf(id));
        user.setMobile(mobile);

        UserHolder.set(user);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        UserHolder.remove();
    }
}
