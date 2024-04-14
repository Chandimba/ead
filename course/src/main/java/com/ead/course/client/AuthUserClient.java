package com.ead.course.client;

import com.ead.course.dtos.CourseUserDTO;
import com.ead.course.dtos.ResponsePageDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.services.UtilsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Component
public class AuthUserClient {

    private final RestTemplate restTemplate;
    private final UtilsService utilsService;

    @Value("${ead.api.url.authuser}")
    String REQUEST_URL_AUTHUSER;


    public Page<UserDTO> getAllUsersByCourse(final UUID userId, Pageable pageable) {

        ResponseEntity<ResponsePageDTO<UserDTO>> result = null;

        String url = REQUEST_URL_AUTHUSER + utilsService.createUrlGetAllUsersByCourse(userId, pageable);

        log.debug("Request URL: {}", url);
        log.info("Request URL: {}", url);

        try {
            ParameterizedTypeReference<ResponsePageDTO<UserDTO>> responseType =
                    new ParameterizedTypeReference<ResponsePageDTO<UserDTO>>() {};

            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

            log.debug("Response Number Of Elements: {}", result.getBody().getContent().size());
        } catch (HttpStatusCodeException ex) {
            log.error("Error request /courses {} ", ex);
        }

        log.info("Ending request /users userId {} ", userId);

        return result.getBody();
    }

    public ResponseEntity<UserDTO> getOneUserById(UUID userId) {
        String url = REQUEST_URL_AUTHUSER + utilsService.createUrlGetOneUsersById(userId);

        return restTemplate.exchange(url, HttpMethod.GET, null, UserDTO.class);
    }

    public void  postSubscriptionUserInCourse(UUID courseId, UUID userId) {
        String url = REQUEST_URL_AUTHUSER + utilsService.createUrlSubscriptionUserInCourse(userId);

        var courseUserDTO = new CourseUserDTO();
        courseUserDTO.setCourseId(courseId);
        courseUserDTO.setUserId(userId);

        String response =  restTemplate.postForObject(url, courseUserDTO, String.class);

        log.debug("Response : {}", response);
    }

    public void deleteCourseUserInAuthUser(UUID courseId) {
        String url = REQUEST_URL_AUTHUSER + utilsService.deleteCourseUserInAuthUser(courseId);

        restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    }
}
