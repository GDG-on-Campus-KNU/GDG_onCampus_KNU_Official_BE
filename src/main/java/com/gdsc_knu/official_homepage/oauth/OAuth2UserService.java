package com.gdsc_knu.official_homepage.oauth;

import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(request);

        String registrationId = request.getClientRegistration().getRegistrationId();
        String accessToken = request.getAccessToken().getTokenValue();
        System.out.println(accessToken);

        String userNameAttributeName = request.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        System.out.println(oAuth2User.getAttributes().toString());
        saveMember(OAuthAttributes.of(oAuth2User.getAttributes()));
        return oAuth2User;
    }
    private void saveMember(OAuthAttributes attributes){
        Optional<Member> member = memberRepository.findByEmail(attributes.getLogin());
        if(member.isEmpty()){
            memberRepository.save(attributes.toMember());
        }
    }
}
