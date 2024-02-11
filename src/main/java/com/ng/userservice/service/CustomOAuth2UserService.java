package com.ng.userservice.service;

import com.ng.userservice.auth.CustomOAuth2User;
import com.ng.userservice.entity.User;
import com.ng.userservice.repository.UserRepository;
import com.ng.userservice.utils.Provider;
import com.ng.userservice.utils.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = super.loadUser(userRequest);


        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");


        User user = userRepository.findByEmail(email)
                .orElseGet(() -> registerNewUser(email, name));

        return new CustomOAuth2User(oauthUser, user);
    }

    private User registerNewUser(String email, String name) {
        log.info("Registering new OAuth2 user: {}", email);
        User newUser = User.builder()
                .email(email)
                .firstname(name)
                .provider(Provider.GOOGLE) // Bu, kullanıcının OAuth2 ile kaydolduğunu belirtir.
                .role(Role.USER) // Yeni kullanıcılar için varsayılan rol
                .build();
        return userRepository.save(newUser);
    }
}
