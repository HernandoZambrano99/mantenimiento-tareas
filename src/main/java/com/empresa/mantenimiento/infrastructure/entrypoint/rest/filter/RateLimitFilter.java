package com.empresa.mantenimiento.infrastructure.entrypoint.rest.filter;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Per-IP bucket-based rate limiter for the login endpoint.
 * 10 requests per minute, refilled greedily. Add more matched paths as needed.
 */
@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!"/api/v1/auth/login".equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String ip = getClientIp(request);
        Bucket bucket = buckets.computeIfAbsent(ip, k -> createBucket());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Too many login attempts. Try again later.\"}");
        }
    }

    private Bucket createBucket() {
        return Bucket.builder()
                .addLimit(limit -> limit.capacity(10).refillGreedy(10, Duration.ofMinutes(1)))
                .build();
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
