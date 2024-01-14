package ru.panyukovnn.sbaserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import java.util.*

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        val successHandler = SavedRequestAwareAuthenticationSuccessHandler()
        successHandler.setTargetUrlParameter("redirectTo")
        successHandler.setDefaultTargetUrl("/")

        return http
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/assets/**").permitAll()
                    .requestMatchers("/login").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { conf ->
                conf.loginPage("/login")
                    .successHandler(successHandler)
            }
            .logout { conf ->
                conf.logoutUrl("/logout")
            }
            .httpBasic(withDefaults())
            .csrf { conf ->
                conf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .ignoringRequestMatchers("/instances", "/actuator/**")
            }
            .rememberMe { conf ->
                conf.key(UUID.randomUUID().toString())
                    .tokenValiditySeconds(1209600)
            }
            .build()
    }
}