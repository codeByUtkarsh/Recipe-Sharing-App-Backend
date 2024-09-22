package com.feature.recipesharingapp.config;


import com.feature.recipesharingapp.data.entities.AccessTokens;
import com.feature.recipesharingapp.data.entities.Users;
import com.feature.recipesharingapp.data.repos.AccesstokenRepository;
import com.feature.recipesharingapp.data.repos.UserRepository;
import com.feature.recipesharingapp.model.response.UserResponse;
import com.feature.recipesharingapp.services.user.UserService;
import com.feature.recipesharingapp.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    private final AccesstokenRepository accesstokenRepository;

    private final UserRepository userRepository;

    private final UserService userService;


    private final UserDetailsService userDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getMethod().equals("OPTIONS")) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        } else {
            final String requestTokenHeader = request.getHeader("Authorization");
            String userId = null;
            String jwtToken = null;
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                try {

                    AccessTokens accessToken = accesstokenRepository.findByTokenAndIsActive(jwtToken, true);
                    if (accessToken == null) {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        filterChain.doFilter(request, response);
                        return;
                    }


                    Optional<Users> userroles = userRepository.findById(accessToken.getUserId());

                    if (userroles.get().getRole().equalsIgnoreCase("ADMIN")
                            || userroles.get().getRole().equalsIgnoreCase("USER")) {

                        String getAccessToken = accessToken.getToken();
                        if (!getAccessToken.isEmpty()) {
                            userId = jwtTokenUtil.getIdFromToken(jwtToken);
                        }
                    } else {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                    }

                } catch (IllegalArgumentException e) {
                    log.warn("Unable to get JWT Token");
                } catch (ExpiredJwtException e) {
                    log.warn("JWT Token has expired");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    filterChain.doFilter(request, response);
                }
            } else {
                log.warn("JWT Token does not begin with Bearer String");
            }

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserResponse userResponse = this.userService.getUserId(userId);

                if (jwtTokenUtil.validateToken(jwtToken, userResponse.getId()).equals(true)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userResponse, null, null);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    log.info("Validation fails");
                }
            }
            filterChain.doFilter(request, response);
        }
    }
}