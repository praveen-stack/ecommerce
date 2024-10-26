package com.ecommerce.cartservice.components;


import com.ecommerce.cartservice.utils.AuthUtil;
import com.ecommerce.cartservice.dtos.AuthorizedUser;
import com.ecommerce.cartservice.exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import java.io.IOException;

@Component
public class AutherisationFilter extends OncePerRequestFilter {

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private AuthUtil authUtil;

    private Claims getClaims(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/auth") || path.startsWith("/public")) {
            chain.doFilter(request, response);  // Bypass your filter for these paths
            return;
        }

        try {
            String jwt = authUtil.getAuthToken(request);
            if (jwt == null) {
                throw new UnauthorizedException("Unauthorized");
            }
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                String userID = null;
                String email = null;
                String name = null;
                try {
                    Claims claims = getClaims(jwt);
                    userID = claims.getSubject();
                    email = claims.get("email", String.class);
                    name = claims.get("name", String.class);
                } catch (Exception e) {
                    throw new UnauthorizedException("Unauthorized");
                }
                AuthorizedUser userAuthrizedDto = new AuthorizedUser();
                userAuthrizedDto.setId(Long.parseLong(userID));
                userAuthrizedDto.setName(name);
                userAuthrizedDto.setEmail(email);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userAuthrizedDto, null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        } catch (UnauthorizedException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT is invalid or expired");
        }
    }
}
