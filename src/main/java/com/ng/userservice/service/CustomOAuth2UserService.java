package com.ng.userservice.service;

import com.ng.userservice.auth.CustomOAuth2User;
import com.ng.userservice.entity.User;
import com.ng.userservice.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

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
                .orElseGet(() -> createUser(email, name));

        return new CustomOAuth2User(oauthUser, user);
    }

    private User createUser(String email, String name) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFirstname(name);
        return userRepository.save(newUser);
    }
}
