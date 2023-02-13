package com.sparta.soomtut.util.security;

import com.sparta.soomtut.entity.Member;
import com.sparta.soomtut.enums.MemberRole;


import lombok.Getter;
import lombok.ToString;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

// lombok
@Getter
@ToString
public class UserDetailsImpl implements UserDetails, OAuth2User {

    private Member member;
    private Map<String, Object> attributes;
    private Map<String, Object> attributesAccount;
    private Map<String, Object> attributesProfile;

    // UserDetails: FormLogin 사용 시
    public UserDetailsImpl(Member member) {
        this.member = member;
    }

    // OAuth2User: OAuth2Login 사용 시
    public UserDetailsImpl(Member member, Map<String, Object> attributes){
        this.member = member;
        this.attributes = attributes;
        this.attributesAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.attributesProfile = (Map<String, Object>) attributesAccount.get("profile");

    }

    public Member getMember(){
        return member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        MemberRole role = member.getMemberRole();
        Set<SimpleGrantedAuthority> set = new HashSet<>();
        set.add(new SimpleGrantedAuthority(role.toString()));
        return set;
    }

    @Override
    public String getPassword() {
        return this.member.getPassword();
    }

    @Override
    public String getUsername() {
        return this.member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public String getProviderId() {
        return attributes.get("id").toString();
    }

    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getName() {
        return this.attributes.get("sub").toString();
    }


}
