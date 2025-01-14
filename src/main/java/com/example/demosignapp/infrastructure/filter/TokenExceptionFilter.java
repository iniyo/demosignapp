package com.example.demosignapp.infrastructure.filter;

import com.example.demosignapp.infrastructure.jwt.TokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class TokenExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenException e) {
            handleTokenException(response, e);
        }
    }

    private void handleTokenException(HttpServletResponse response, TokenException e) throws IOException {
        // JSON 응답으로 예외 처리
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try (PrintWriter writer = response.getWriter()) {
            writer.write("{\"error\": \"Unauthorized\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}