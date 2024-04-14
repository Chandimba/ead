package com.ead.course.services;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UtilsService {
    String createUrlGetAllUsersByCourse(UUID userId, Pageable pageable);
    String createUrlGetOneUsersById(UUID userId);
    String createUrlSubscriptionUserInCourse(UUID userId);
    String deleteCourseUserInAuthUser(UUID courseId);
}
