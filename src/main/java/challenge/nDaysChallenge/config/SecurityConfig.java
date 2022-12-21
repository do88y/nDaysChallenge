package challenge.nDaysChallenge.config;

import challenge.nDaysChallenge.jwt.TokenProvider;
import challenge.nDaysChallenge.security.CustomAccessDeniedHandler;
import challenge.nDaysChallenge.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Spring Security 미적용 => 테스트 시 자유롭게 접근 가능
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }

    //Spring Security 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http

            .csrf().disable() //CSRF 방어 기능 비활성화 (쿠키 사용 안 하므로)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            //세션 쿠키 방식 X

            .and()
            .cors().configurationSource(corsConfigurationSource())

            //Security Filter에서 발생하는 인증, 인가 오류 처리
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(customAuthenticationEntryPoint) //인증 에러 핸들링
            .accessDeniedHandler(customAccessDeniedHandler) //인가 에러 핸들링

                // 테스트 위해 주석 처리함
//                .and()
//            .formLogin()
//            .loginPage("/auth/login")
//            .usernameParameter("id") ////로그인 아이디의 name 속성값
//            .passwordParameter("pw")
//            .defaultSuccessUrl("/")


//            .and()
//            .headers()
//            .frameOptions()
//            .sameOrigin()

            //인증,인가에 대한 path matching & Preflight Request 허용
            .and()
            .authorizeRequests() //요청에 대한 권한 설정
            .antMatchers("/","/auth/**").permitAll() //로그인, 회원가입 api는 인증 정보 요구 X
            .anyRequest().authenticated() //그외 모든 요청에 대해 인증 필요

            //JwtFilter를 addFilterBefore로 등록한 JwtSecurityConfig 적용
            //=> JwtFilter을 기존 필터체인에 끼워넣음
            .and()
            .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();

    }

//    CORS 허용
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(false); //cross origin으로부터 쿠키 받을지
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080")); //허용할 origin
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST")); //허용할 http method
        configuration.addAllowedOrigin("http://43.201.59.236:3000"); ////
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("accessToken");
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    //AuthenticationPrincipal 멤버객체 컨트롤러단에서부터 영속화
    @Bean
    public FilterRegistrationBean<OpenEntityManagerInViewFilter> openEntityManagerInViewFilter() {

        FilterRegistrationBean<OpenEntityManagerInViewFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new OpenEntityManagerInViewFilter());
        filterFilterRegistrationBean.setOrder(Integer.MIN_VALUE);
        return filterFilterRegistrationBean;

    }

}
