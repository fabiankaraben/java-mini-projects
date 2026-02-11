package com.example.multitenant.interceptor;

import com.example.multitenant.config.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tenantId = request.getHeader(TENANT_HEADER);
        if (tenantId != null && !tenantId.isBlank()) {
            TenantContext.setTenantId(tenantId);
        } else {
            // Default tenant or error? For this mini-project, let's treat it as "default" or just fail if strict.
            // Let's allow it to be null, but maybe log a warning or set a default if needed.
            // For strict multi-tenancy, usually we enforce it.
            // Let's set a default "public" tenant if missing for simplicity, or just leave it null.
            // If null, the filter might not apply or we might want to throw 400.
            // Let's enforce it for the demo to ensure isolation is tested.
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "X-Tenant-ID header is missing");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        TenantContext.clear();
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TenantContext.clear();
    }
}
