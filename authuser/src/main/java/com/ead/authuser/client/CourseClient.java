package com.ead.authuser.client;

import com.ead.authuser.dto.CourseDTO;
import com.ead.authuser.dto.ResponsePageDTO;
import com.ead.authuser.services.UtilsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Component
public class CourseClient {

    private final RestTemplate restTemplate;
    private final UtilsService utilsService;

    @Value("${ead.api.url.course}")
    String REQUEST_URL_COURSE;

    //@Retry(name = "retryInstance", fallbackMethod = "cirduitBreakerFallback")
    @CircuitBreaker(name = "circuitbreakerInstance"/*, fallbackMethod = "retryFallback"*/)
    public Page<CourseDTO> getAllCoursesByUser(final UUID userId, Pageable pageable, String token) {

        ResponseEntity<ResponsePageDTO<CourseDTO>> result = null;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        String url = REQUEST_URL_COURSE + utilsService.createUrl(userId, pageable);

        log.debug("Request URL: {}", url);
        log.info("Request URL: {}", url);
        System.out.println("--- Start Request ao Course Microservice ---");

        ParameterizedTypeReference<ResponsePageDTO<CourseDTO>> responseType = new ParameterizedTypeReference<>() {};

        result = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);

        log.debug("Response Number Of Elements: {}", result.getBody().getContent().size());


        log.info("Ending request /courses userId {} ", userId);

        return result.getBody();
    }

    public Page<CourseDTO> cirduitBreakerFallback(final UUID userId, Pageable pageable, Throwable exception) {
        log.error("Inside cirduit breaker fallback retryFallback, cause - {}", exception.toString());
        List<CourseDTO> courseDTOS = new ArrayList<>();
        return new PageImpl<>(courseDTOS);
    }

    public Page<CourseDTO> retryFallback(final UUID userId, Pageable pageable, Throwable exception) {
        log.error("Inside retry retryFallback, cause - {}", exception.toString());
        List<CourseDTO> courseDTOS = new ArrayList<>();
        return new PageImpl<>(courseDTOS);
    }

}
