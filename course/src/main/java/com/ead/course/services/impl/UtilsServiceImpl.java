package com.ead.course.services.impl;

import com.ead.course.services.UtilsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {

    @Override
    public String createUrlGetAllUsersByCourse(UUID userId, Pageable pageable) {
        return "/users?courseId=" + userId
                + "&page=" + pageable.getPageNumber()
                + "&size=" + pageable.getPageSize()
                + "&sort=" + pageable.getSort().toString().replaceAll(":", ",")
                .replaceAll(" ", "");
    }

    @Override
    public String createUrlGetOneUsersById(UUID userId) {
        return "/users/" + userId ;
    }

    @Override
    public String createUrlSubscriptionUserInCourse(UUID userId) {
        return "/users/" + userId + "/courses/subscription";
    }

    @Override
    public String deleteCourseUserInAuthUser(UUID courseId) {
        return "/users/courses/" + courseId;
    }
}
