package com.ezeeinfo.service;

import com.ezeeinfo.issuemanager.IssueManagerManager;
import com.ezeeinfo.issuemanager.model.AppUser;
import com.ezeeinfo.issuemanager.model.Roles;
import com.ezeeinfo.issuemanager.model.UserRoles;
import com.ezeeinfo.issuemanager.store.AppUserStore;
import com.ezeeinfo.issuemanager.store.RolesStore;
import com.ezeeinfo.issuemanager.store.UserRolesStore;
import io.jsonwebtoken.lang.Collections;
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

/**
 * CustomUserDetailsService class.
 * UserDetailsService interface
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * AppUserStore declaration.
     */

    private final AppUserStore appUserStore;
    /**
     * UserRolesStore declaration.
     */
    private final UserRolesStore userRolesStore;

    /**
     * RolesStore declaration.
     */
    private final RolesStore rolesStore;

    /**
     * CustomUserDetailsService method.
     *
     * @param dataSource
     */
    @Autowired
    public CustomUserDetailsService(final DataSource dataSource) {
        appUserStore = IssueManagerManager.getManager(dataSource)
                .getAppUserStore();
        userRolesStore = IssueManagerManager.getManager(dataSource)
                .getUserRolesStore();
        rolesStore = IssueManagerManager.getManager(dataSource)
                .getRolesStore();
    }

    /**
     * UserDetails method.
     *
     * @param username
     * @return user details
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {

        AppUser appUser = null;
        List<UserRoles> roles = null;
        try {
            List<AppUser> appUsers =
                    appUserStore.select(AppUserStore.username()
                            .eq(username)).execute();
            if (!Collections.isEmpty(appUsers)) {
                appUser = appUsers.get(0);
            }
            roles = userRolesStore.select(UserRolesStore.userId()
                    .eq(appUser.getId())).execute();

        } catch (SQLException e) {
            throw new UsernameNotFoundException("Username not found");
        }
        return new User(appUser.getUsername(), appUser.getPassword(),
                mapRolesToAuthorities(roles));
    }

    /**
     * @param roles
     * @return Autority collection
     */
    private Collection<GrantedAuthority> mapRolesToAuthorities(
            final List<UserRoles> roles) {
        return roles.stream().map(role -> {
            try {
                return new SimpleGrantedAuthority(
                        rolesStore.select(role.getRoleId()).get().getName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    /**
     * @param user
     * @param role
     * @return the user
     */
    public AppUser registerUser(final AppUser user, final String role) {
        AppUser createdUser = null;
        try {
            List<AppUser> foundUsers = appUserStore.select(AppUserStore
                    .username().eq(user.getUsername())).execute();
            if (!Collections.isEmpty(foundUsers)) {
                return foundUsers.get(0);
            }
            createdUser = appUserStore.insert().values(user).returning();
            List<Roles> roles = rolesStore.select(RolesStore.name()
                    .eq(role)).execute();
            if (!Collections.isEmpty(roles)) {
                UserRoles userRoles = new UserRoles();
                userRoles.setUserId(createdUser.getId());
                userRoles.setRoleId(roles.get(0).getId());
                userRolesStore.insert().values(userRoles).execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return createdUser;
    }
}
