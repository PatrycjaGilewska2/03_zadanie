package pl.jstk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.inMemoryAuthentication()
		.withUser("user").password(passwordEncoder().encode("user")).roles("USER")
		.and()
		.withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
          .authorizeRequests()
          .antMatchers("/books/delete/**").hasRole("ADMIN")
          .antMatchers("/books/add").hasRole("ADMIN")
          .anyRequest().permitAll()
          .and()
          .formLogin()
          .loginPage("/login")
          .defaultSuccessUrl("/welcome")
          .failureUrl("/loginfailed")
          .and()
          .logout().logoutUrl("/logout").logoutSuccessUrl("/welcome")
          .and()
          .exceptionHandling().accessDeniedPage("/403");
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
