package com.youlai.system.filter;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.youlai.system.common.constant.SecurityConstants;
import com.youlai.system.common.result.ResultCode;
import com.youlai.system.common.util.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * 验证码校验过滤器
 *
 * @author YANG FUCHAO
 * @since 2022/10/1
 */
public class VerifyCodeFilter extends OncePerRequestFilter {

    private static final AntPathRequestMatcher LOGIN_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(SecurityConstants.LOGIN_PATH, "POST");
    private static final AntPathRequestMatcher SIGN_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(SecurityConstants.SIGN_PATH, "POST");

    public static final String VERIFY_CODE_PARAM_KEY = "verifyCode";
    public static final String VERIFY_CODE_KEY_PARAM_KEY = "verifyCodeKey";

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 检验登录接口的验证码
        if (LOGIN_PATH_REQUEST_MATCHER.matches(request)||SIGN_PATH_REQUEST_MATCHER.matches(request)) {
            // 请求中的验证码
            String requestVerifyCode = request.getParameter(VERIFY_CODE_PARAM_KEY);
            System.out.println("进入了消炎");
            System.out.println(SIGN_PATH_REQUEST_MATCHER.matches(request));
            // TODO 兼容 2.0.0 无验证码版本，后续移除
            if (StrUtil.isBlank(requestVerifyCode)) {
                // 非登录接口放行
                chain.doFilter(request, response);
                return;
            }
            System.out.println("缓存中的验证码");
            // 缓存中的验证码
            RedisTemplate redisTemplate = SpringUtil.getBean("redisTemplate", RedisTemplate.class);
            String verifyCodeKey = request.getParameter(VERIFY_CODE_KEY_PARAM_KEY);
            Object cacheVerifyCode = redisTemplate.opsForValue().get(SecurityConstants.VERIFY_CODE_CACHE_PREFIX + verifyCodeKey);
            if (cacheVerifyCode == null) {
                ResponseUtils.writeErrMsg(response, ResultCode.VERIFY_CODE_TIMEOUT);
            } else {
                System.out.println("验证码比对");
                // 验证码比对
                if (StrUtil.equals(requestVerifyCode, Convert.toStr(cacheVerifyCode))) {
                    System.out.println("对");
                    chain.doFilter(request, response);
                } else {
                    System.out.println("错");
                    ResponseUtils.writeErrMsg(response, ResultCode.VERIFY_CODE_ERROR);
                }
            }
        } else {
            // 非登录接口放行
            chain.doFilter(request, response);
        }
    }

}
