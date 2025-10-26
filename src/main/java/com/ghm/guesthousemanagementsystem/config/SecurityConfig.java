package com.ghm.guesthousemanagementsystem.config;

import com.ghm.guesthousemanagementsystem.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return builder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/uploads/**").permitAll()

                                .requestMatchers(HttpMethod.GET, "/api/admin-users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/admin-users/**").permitAll()
                                .requestMatchers(HttpMethod.PATCH, "/api/admin-users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/admin-users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/admin-users/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/api/content-pages/**").permitAll()

                                //Property access
                                .requestMatchers(HttpMethod.GET, "/api/property/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/property/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/property/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/property/**").permitAll()
                                .requestMatchers(HttpMethod.PATCH, "/api/property/**").hasRole("ADMIN")

                                //Booking availability access
                                .requestMatchers(HttpMethod.GET, "/api/bookings/availability").permitAll()

                                //Admin Booking access
                                .requestMatchers(HttpMethod.POST, "/api/bookings/admin/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/bookings/admin/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/api/bookings/admin/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/bookings/admin/*").hasRole("ADMIN")

                                //Guest Booking access
                                .requestMatchers(HttpMethod.POST, "/api/bookings/guest/**").permitAll()
                                .requestMatchers(HttpMethod.PATCH, "/api/bookings/guest/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/bookings/guest/**").permitAll()

                                //Room access
                                .requestMatchers(HttpMethod.GET, "/api/property/*/rooms/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/property/*/add-new-room").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/api/property/*/rooms/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/property/*/rooms/**").permitAll()

                                // Photo
                                .requestMatchers(HttpMethod.GET, "/api/photos/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/photos/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/photos/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/api/photos/property/*").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/photos/room/*").permitAll()

                                .requestMatchers("/api/booking-rooms").hasRole("ADMIN")
                                .requestMatchers("/api/status-history").hasRole("ADMIN")
                                .requestMatchers("/api/temporary-token").hasRole("ADMIN")

                                // =============== Naveen parts are below

                                //Amenities - Admin
                                .requestMatchers(HttpMethod.GET, "/api/amenities").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/amenities").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/amenities/**").hasRole("ADMIN")

                                //Room Amenities - Temporarily allow all for testing
                                .requestMatchers(HttpMethod.GET, "/api/room-amenities/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/room-amenities/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/room-amenities/**").permitAll()

                                //Addon - Admin
                                .requestMatchers(HttpMethod.GET, "/api/addons", "/api/addons/search").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/addons/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/addons/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/api/addons/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/addons/**").hasRole("ADMIN")


                                //Addon - Guest
                                .requestMatchers(HttpMethod.GET,
                                        "/api/addons/active",
                                        "/api/addons/*",
                                        "/api/addons/name/**",
                                        "/api/addons/search/active"
                                ).permitAll()

                                //Review - Admin
                                .requestMatchers(HttpMethod.GET,
                                        "/api/reviews/admin",
                                        "/api/reviews/admin/reference/{referenceId}",
                                        "/api/reviews/admin/property/{propertyId}",
                                        "/api/reviews/admin/search",
                                        "/api/reviews/admin/property-ratings"
                                ).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/reviews/admin/{referenceId}").hasRole("ADMIN")

                                //Review - Guest
                                .requestMatchers(HttpMethod.POST, "/api/reviews/guest").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/reviews/guest/{referenceId}").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/reviews/guest/{referenceId}").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/reviews/guest/{referenceId}").permitAll()

                                //Review - Public
                                .requestMatchers(HttpMethod.GET,
                                        "/api/reviews/public/property/{propertyId}/rating",
                                        "/api/reviews/public/booking/{referenceId}/property-rating",
                                        "/api/reviews/public/statistics"
                                ).permitAll()

                                //Payment Proofs - Admin
                                .requestMatchers(HttpMethod.GET,
                                        "/api/payment-proofs/id/{paymentProofId}",
                                        "/api/payment-proofs/id/{paymentProofId}/download"
                                ).hasRole("ADMIN")

                                .requestMatchers(HttpMethod.DELETE,
                                        "/api/payment-proofs/id/{paymentProofId}"
                                ).hasRole("ADMIN")

                                //Payment Proof - Guest
                                .requestMatchers(HttpMethod.POST, "/api/payment-proofs").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/payment-proofs/{referenceId}").permitAll()
                                .requestMatchers(HttpMethod.GET,
                                        "/api/payment-proofs/{referenceId}",
                                        "/api/payment-proofs/{referenceId}/download",
                                        "/api/payment-proofs/{referenceId}/exists"
                                ).permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/payment-proofs/{referenceId}").permitAll()


                                .requestMatchers("/api/**").authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}