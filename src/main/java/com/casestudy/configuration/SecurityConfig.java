package com.casestudy.configuration;


import com.casestudy.service.user.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    AppUserService appUserService;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService((UserDetailsService) appUserService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/home","/edit-user/**").access("hasRole('MEMBER') or hasRole('ADMIN') or hasRole('MOD') or hasRole('SUS')")
                .antMatchers("/create-topic/**","/edit-topic/**").access("hasRole('MEMBER') or hasRole('ADMIN') or hasRole('MOD')")
                .antMatchers("/delete-topic-post/**","/categories/**","/view-create-cate/**"
                ,"/create-or-update-cate/**","/edit-cate/**","/delete-cate/**").access("hasRole('ADMIN') or hasRole('MOD')")
                .antMatchers("/admin/**", "/list-user/**", "/delete-user/**").access("hasRole('ADMIN')")
                .antMatchers("/dba/**").access("hasRole('ADMIN') and hasRole('DBA')")
                .and().formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureUrl("/login_error")
//                .failureUrl("/login?error=true")
                .successHandler(new CustomSuccessHandler())
//                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and().exceptionHandling().accessDeniedPage("/accessDenied");
        http.csrf().disable();
//        http.authorizeRequests().and().rememberMe();

        http.logout().logoutSuccessUrl("/");
//                .rememberMe().tokenRepository(this.persistentTokenRepository()) //
//                .tokenValiditySeconds(1 * 24 * 60 * 60);


//                .successHandler(new CustomSuccessHandler())
//                .usernameParameter("ssoID").passwordParameter("password")
//                .and().csrf()
//                .and().exceptionHandling().accessDeniedPage("/accessDenied");
    }
}