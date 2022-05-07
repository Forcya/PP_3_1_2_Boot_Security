package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ru.kata.spring.boot_security.demo.service.UserDetailServiceImpl;

@Configuration // не обязательная аннотация.
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private final SuccessUserHandler successUserHandler;
    private final PasswordEncoderConfig passwordEncoder;
    private final UserDetailServiceImpl userDetailService;

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, PasswordEncoderConfig passwordEncoder, UserDetailServiceImpl userDetailService) {
        this.successUserHandler = successUserHandler;
        this.passwordEncoder = passwordEncoder;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().hasAnyRole("ADMIN", "USER")
                .and()
                .formLogin().successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }


    //Для использования данных для аутентификации из БД
    //Мы отдаем методу логин и пароль, а он говорит, существует такой пользователь или нет
    //Если пользователь существует, то он кладет его в Spring SecurityContext - класс-хранилище пользователей
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider () {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder.passwordEncoder()); //Используй преобразование паролей
        //Предоставляет провайдеру пользователей, для сверки с введенными данными
        daoAuthenticationProvider.setUserDetailsService(userDetailService); //получаем преобразованного пользователя из БД в Spring пользователя
        return daoAuthenticationProvider;
    }
}