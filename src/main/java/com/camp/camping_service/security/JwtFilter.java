package com.camp.camping_service.security;

import com.camp.camping_service.dto.common.UserPrincipal;
import com.camp.camping_service.services.UserService;
import com.camp.camping_service.utils.ClerkUtil;
import com.clerk.backend_api.helpers.security.models.RequestState;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final ClerkUtil clerkUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @Nonnull
            HttpServletRequest request,
            @Nonnull
            HttpServletResponse response,
            @Nonnull
            FilterChain filterChain) throws ServletException, IOException {

        final RequestState requestState = this.clerkUtil.getRequestState(request);
        final String username = this.clerkUtil.getUsernameFromToken(requestState);

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserPrincipal userPrincipal = (UserPrincipal) this.userService.loadUserByUsername(username);
            if(this.clerkUtil.verifyToken(requestState)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userPrincipal,
                        null,
                        userPrincipal.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            filterChain.doFilter(request,response);
            return;
        }

        filterChain.doFilter(request,response);
    }
}
