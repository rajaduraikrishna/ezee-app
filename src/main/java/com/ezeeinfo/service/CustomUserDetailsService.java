package com.ezeeinfo.service;

import com.ezeeinfo.issuemanager.IssueManagerManager;
import com.ezeeinfo.issuemanager.model.AppUser;
import com.ezeeinfo.issuemanager.model.UserRoles;
import com.ezeeinfo.issuemanager.store.AppUserStore;
import com.ezeeinfo.issuemanager.store.RolesStore;
import com.ezeeinfo.issuemanager.store.UserRolesStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final AppUserStore appUserStore;
    private final UserRolesStore userRolesStore;

    private final RolesStore rolesStore;
    @Autowired
    public CustomUserDetailsService(final DataSource dataSource)
    {
        appUserStore = IssueManagerManager.getManager(dataSource).getAppUserStore();
        userRolesStore = IssueManagerManager.getManager(dataSource).getUserRolesStore();
        rolesStore = IssueManagerManager.getManager(dataSource).getRolesStore();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser appUser = null;
        List<UserRoles> roles = null;
        try {
            appUser = appUserStore.select(AppUserStore.username().eq(username)).execute().getFirst();
            roles = userRolesStore.select(UserRolesStore.userId().eq(appUser.getId())).execute();

        } catch (SQLException e) {
            throw new UsernameNotFoundException("Username not found");
        }
        return new User(appUser.getUsername(), appUser.getPassword(), mapRolesToAuthorities(roles));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<UserRoles> roles) {
        return roles.stream().map(role -> {
            try {
                return new SimpleGrantedAuthority(rolesStore.select(role.getRoleId()).get().getName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }
}