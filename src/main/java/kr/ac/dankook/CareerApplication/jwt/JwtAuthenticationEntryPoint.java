package kr.ac.dankook.CareerApplication.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.ac.dankook.CareerApplication.exception.TokenErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final JwtErrorResponseHandler jwtErrorResponseHandler;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        System.out.print(request.getRequestURI());
        System.out.println(authException.getMessage());
        JWTVerificationException jwtVerificationException =
                (JWTVerificationException) request.getAttribute("exception");
        if (authException instanceof BadCredentialsException) {
            jwtErrorResponseHandler.sendErrorResponseProcess(
                    response,
                    TokenErrorCode.UNAUTHORIZED_INVALID_CREDENTIALS
            );
            return;
        }
        if (jwtVerificationException instanceof TokenExpiredException) {
            jwtErrorResponseHandler.sendErrorResponseProcess(response,TokenErrorCode.CLIENT_CLOSED_REQUEST_ACCESS_TOKEN_EXPIRED);
            return;
        }
        if (jwtVerificationException != null) {
            jwtErrorResponseHandler.sendErrorResponseProcess(response, TokenErrorCode.UNAUTHORIZED_INVALID_ACCESS_TOKEN);
            return;
        }
        jwtErrorResponseHandler.sendErrorResponseProcess(response, TokenErrorCode.INTERNAL_SERVER_ERROR_TOKEN_PARSING);
    }

}
