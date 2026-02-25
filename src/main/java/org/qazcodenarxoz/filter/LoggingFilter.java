package org.qazcodenarxoz.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebFilter("/*")
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        long start = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();

        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpResp = (HttpServletResponse) resp;

        StatusExposingResponseWrapper wrappedResp = new StatusExposingResponseWrapper(httpResp);

        System.out.printf("[REQUEST] %s %s requestId=%s%n",
                httpReq.getMethod(), httpReq.getRequestURI(), requestId);

        chain.doFilter(req, wrappedResp);

        long duration = System.currentTimeMillis() - start;
        System.out.printf("[RESPONSE] %s %s → %d (%d ms) requestId=%s%n",
                httpReq.getMethod(), httpReq.getRequestURI(),
                wrappedResp.getStatus(), duration, requestId);
    }
}