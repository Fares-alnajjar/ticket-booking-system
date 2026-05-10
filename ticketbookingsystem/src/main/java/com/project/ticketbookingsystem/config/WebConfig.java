package com.project.ticketbookingsystem.config;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.project.ticketbookingsystem.model.UserEntity;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 1. Admin-only pages
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                HttpSession session = request.getSession();
                UserEntity user = (UserEntity) session.getAttribute("loggedInUser");

                if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
                    response.sendRedirect("/sign_in");
                    return false;
                }
                return true;
            }
        }).addPathPatterns("/admin/**");

        // 2. User-only pages (logged in, any role)
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                HttpSession session = request.getSession();
                UserEntity user = (UserEntity) session.getAttribute("loggedInUser");

                if (user == null) {
                    response.sendRedirect("/sign_in");
                    return false;
                }
                return true;
            }
        }).addPathPatterns("/my-tickets/**", "/user/**");
        // Add any other user-only paths here
    }
}