package com.iny.side.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("successHandler")
public class FormAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        setDefaultTargetUrl("/");

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
            String redirectUrl = savedRequest.getRedirectUrl();

            // 필터 조건: 이상하거나 위험한 redirect 는 기본 경로로 리디렉트
            if (redirectUrl == null
                    || redirectUrl.contains("/.well-known/")                // 크롬 DevTools 자동 요청
                    || redirectUrl.endsWith(".json")                        // API 요청
                    || redirectUrl.contains("/error")                       // 에러 페이지
                    || redirectUrl.contains("favicon")                      // favicon.ico
                    || redirectUrl.contains("service-worker")) {            // 서비스워커 요청
                redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
            } else {
                redirectStrategy.sendRedirect(request, response, redirectUrl);
            }

        } else {
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        }
    }
}
