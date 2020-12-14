package com.project.cashflow.auth;


//TODO Looking good bro! I like that you have stuff in order and a good structure :)


//TODO 1. Regarding packaging by layer which is the current structure of this project. I would recommend http://www.javapractices.com/topic/TopicAction.do?Id=205
//TODO packaging by feature. So your entire app will consist of feature packages, each package containing all stuff that is required to make it complete. Global stuff imported
// TODO The reason why I would do this is because it is easier for some other developer to quickly learn about the domain of the app and not the actual tech specific layers that we already know about.
import com.project.cashflow.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@NoArgsConstructor
public class ApplicationUser implements UserDetails {

    private User user;


    public ApplicationUser(User user) {
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }


}
