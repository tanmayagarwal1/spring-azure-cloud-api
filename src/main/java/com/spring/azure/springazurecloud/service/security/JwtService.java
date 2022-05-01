package com.spring.azure.springazurecloud.service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.dto.token.AccessTokenDto;
import com.spring.azure.springazurecloud.dto.response.ResponseDto;
import com.spring.azure.springazurecloud.models.client.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtService {

    private final String key;
    private final ClientService clientService;

    public void authorizeJwt(String authHeader){
        String jwt = authHeader.substring(Constants.SECURITY.AUTH_HEADER.length());
        Algorithm algorithm = Algorithm.HMAC256(key.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(jwt);
        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(roles).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    public AccessTokenDto generateAccessAndRefreshTokens(String issuer, Authentication authResult, long expiryInMinutes){
        User user = (User) authResult.getPrincipal();
        String accessToken = generateAccessToken(user, issuer, expiryInMinutes,false, null);
        String refreshToken = generateRefreshToken(user, issuer);
        return AccessTokenDto.builder()
                .success(true)
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .validity_in_ms(expiryInMinutes * 60 * 1000)
                .build();

    }

    private String generateAccessToken(User user, String issuer, long expiryInMinutes, boolean fromRefreshToken, Client client){
        return JWT.create()
                .withSubject(fromRefreshToken ? client.getUsername()
                        : user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiryInMinutes * 60 * 1000))
                .withIssuer(issuer)
                .withClaim("roles", !fromRefreshToken ? user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
                        : new ArrayList<>(Collections.singleton(client.getRole().toString())))
                .sign(getAlgorithm());
    }

    private String generateRefreshToken(User user, String issuer){
        return  JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()
                        + Constants.SECURITY.REFRESH_TOKEN_EXPIRY_MIN * 60 * 1000))
                .withIssuer(issuer)
                .sign(getAlgorithm());

    }

    public Optional<ResponseDto> handleRefreshTokenRequest(String authHeader, String issuer, long expiryInMinutes){
        String jwt = authHeader.substring(Constants.SECURITY.AUTH_HEADER.length());
        Algorithm algorithm = Algorithm.HMAC256(key.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(jwt);
        String username = decodedJWT.getSubject();
        Client client = clientService.getClient(username);

        String accessToken = generateAccessToken(null, issuer, expiryInMinutes, true, client);
        return Optional.of(AccessTokenDto.builder()
                .success(true)
                .access_token(accessToken)
                .refresh_token(jwt)
                .validity_in_ms(expiryInMinutes * 60 * 1000)
                .build());
    }

    private Algorithm getAlgorithm(){
        return Algorithm.HMAC256(key);
    }
}
