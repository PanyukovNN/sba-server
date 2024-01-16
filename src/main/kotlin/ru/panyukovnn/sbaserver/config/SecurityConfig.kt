package ru.panyukovnn.sbaserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        val successHandler = SavedRequestAwareAuthenticationSuccessHandler()
        successHandler.setTargetUrlParameter("redirectTo")
        successHandler.setDefaultTargetUrl("/")

        http
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/assets/**").permitAll()
                    .requestMatchers("/login").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { fl ->
                fl.loginPage("/login")
                    .successHandler(successHandler)
            }
            .logout { lo ->
                lo.logoutUrl("/logout")
            }
            .httpBasic(Customizer.withDefaults())
            .csrf { csrf ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .ignoringRequestMatchers("/instances", "/actuator/**")
            }

        return http.build()
    }
}