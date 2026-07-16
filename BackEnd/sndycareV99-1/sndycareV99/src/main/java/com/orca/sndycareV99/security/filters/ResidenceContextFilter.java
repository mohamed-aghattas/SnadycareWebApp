package com.orca.sndycareV99.security.filters;

import com.orca.sndycareV99.security.user.CustomUserPrincipal;
import com.orca.sndycareV99.service.ResidanceContextService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ResidenceContextFilter extends OncePerRequestFilter {

    private final ResidanceContextService residanceContextService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/v1/auth")
                || requestURI.equals("/error")
                || requestURI.equals("/api/v1/residence/select-residence")
                || requestURI.equals("/api/v1/residence")
                || requestURI.startsWith("/api/v1/location") ) {

            filterChain.doFilter(request, response);
            return;
        }

        String value = request.getHeader("X-Residence-Id");


        if (value == null || value.trim().isEmpty()) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Missing required 'X-Residence-Id' header for this operation.");
            return;
        }

        try {
            Long residanceId = Long.parseLong(value);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


            if (authentication != null && authentication.isAuthenticated()
                    && authentication.getPrincipal() instanceof CustomUserPrincipal) {

                CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

                residanceContextService.initialize(residanceId, principal.getUser().getId());
            } else {

                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "User authentication is missing or invalid.");
                return;
            }

        } catch (NumberFormatException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid 'X-Residence-Id' format. It must be a number.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonError = String.format("{status: %d, error: Bad Request, message: %s}", status, message);
        response.getWriter().write(jsonError);
    }
}