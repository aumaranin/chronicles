package bmstu.chronicles.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;


    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    public WebSecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT login, password, enabled FROM person WHERE login=?")
                .authoritiesByUsernameQuery("SELECT login, role FROM person WHERE login=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.authorizeRequests()

                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/leader/**").hasAnyRole("ADMIN", "LEADER")
                .antMatchers("/climber/**").hasAnyRole("ADMIN", "LEADER", "CLIMBER")
                .antMatchers("/").permitAll()
                .and().formLogin().loginPage("/login").successHandler(authenticationSuccessHandler);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder()
    {
        return NoOpPasswordEncoder.getInstance();
    }
}