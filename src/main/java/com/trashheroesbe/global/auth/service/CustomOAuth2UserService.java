package com.trashheroesbe.global.auth.service;

import com.trashheroesbe.feature.user.domain.type.AuthProvider;
import com.trashheroesbe.feature.user.domain.type.Role;
import com.trashheroesbe.feature.user.domain.User;
import com.trashheroesbe.feature.user.infrastructure.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
            .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 카카오 사용자 정보 처리
        if ("kakao".equals(registrationId)) {
            return processKakaoUser(attributes, userNameAttributeName);
        }

        throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
    }

    private OAuth2User processKakaoUser(
        Map<String, Object> attributes,
        String userNameAttributeName
    ) {
        String kakaoId = String.valueOf(attributes.get(userNameAttributeName));

        @SuppressWarnings("unchecked")
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        @SuppressWarnings("unchecked")
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String nickname = (String) profile.get("nickname");
        String profileImageUrl = (String) profile.get("profile_image_url");

        User user = userRepository.findByKakaoId(kakaoId)
            .orElseGet(() -> createKakaoUser(kakaoId, nickname, profileImageUrl));

        // userNameAttributeName == "id"
        return new DefaultOAuth2User(
            user.getRole().toAuthorities(),
            attributes,
            userNameAttributeName
        );
    }

    private User createKakaoUser(
        String kakaoId,
        String nickname,
        String profileImageUrl
    ) {
        User newUser = User.builder()
            .kakaoId(kakaoId)
            .nickname(nickname)
            .profileImageUrl(profileImageUrl)
            .provider(AuthProvider.KAKAO)
            .role(Role.USER)
            .build();

        return userRepository.save(newUser);
    }
}
