package challenge.nDaysChallenge.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    //React 와의 SOP 문제 해결//
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(false) //쿠키 받을지
                .maxAge(MAX_AGE_SECS);
    }

}
