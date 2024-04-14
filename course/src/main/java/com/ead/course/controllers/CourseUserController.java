package com.ead.course.controllers;

import com.ead.course.dtos.SubscriptionDTO;
import com.ead.course.enums.UserStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.UserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;
import com.ead.course.specifications.SpecificationTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    private final CourseService courseService;
    private final UserService userService;

    @GetMapping("/courses/{courseId}/users")
    ResponseEntity<Object> getAllUsersByCourse(
            SpecificationTemplate.UsereSpec spec,
            @PathVariable UUID courseId,
            @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable) {

        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                userService.findAll(SpecificationTemplate.userCourseId(courseId).and(spec), pageable));
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    ResponseEntity<Object> saveSubscritionUserInCourse(@PathVariable UUID courseId,
                                                       @RequestBody @Valid SubscriptionDTO subscriptionDTO) {

        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }

        if(courseService.existsByCourseAndUser(courseId, subscriptionDTO.getUserId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists.");
        }

        Optional<UserModel> userModelOptional = userService.findById(subscriptionDTO.getUserId());
        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        if(userModelOptional.get().getUserStatus().equals(UserStatus.BLOCKED.toString())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is blocked.");
        }

        courseService.saveSubscriptionUserInCourse(courseModelOptional.get().getCourseId(),
                userModelOptional.get().getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body("Subscription created successfully.");
    }

}
