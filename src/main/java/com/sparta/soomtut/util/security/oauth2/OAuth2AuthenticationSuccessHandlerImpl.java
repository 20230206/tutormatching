package com.sparta.soomtut.util.security.oauth2;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.sparta.soomtut.util.jwt.JwtProvider;
import com.sparta.soomtut.util.security.UserDetailsImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandlerImpl extends SimpleUrlAuthenticationSuccessHandler  {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    throws IOException, ServletException {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        String token = jwtProvider.createToken(user.getUsername(), user.getMember().getMemberRole());

        // react의 url parameter를 이용하기위해서 해당 url 주소로 토큰값과 함께 redirect 시켜준다.
        String frontend = "http://localhost:3000/setsignin/"+token;

        response.sendRedirect(frontend);
    }

}