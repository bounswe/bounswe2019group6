package cmpe451.group6;

import cmpe451.group6.authorization.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class Util {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public String unwrapUsername(HttpServletRequest req){
        String token = jwtTokenProvider.resolveToken(req);
        return token == null ? null : jwtTokenProvider.getUsername(token);
    }
}
