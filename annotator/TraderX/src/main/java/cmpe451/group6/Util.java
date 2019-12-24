package cmpe451.group6;

import cmpe451.group6.authorization.security.JwtTokenProvider;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class Util {

    final JwtTokenProvider jwtTokenProvider;

    public Util(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String unwrapUsername(HttpServletRequest req){
        String token = jwtTokenProvider.resolveToken(req);
        return token == null ? null : jwtTokenProvider.getUsername(token);
    }
}
