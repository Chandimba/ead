package com.ead.authuser.controllers;

import com.ead.authuser.client.CourseClient;
import com.ead.authuser.dto.CourseDTO;
import com.ead.authuser.dto.UserCourseDTO;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
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
public class UserCourseController {

    private final CourseClient courseClient;
    private final UserService userService;
    private final UserCourseService userCourseService;

    @GetMapping("/users/{userId}/courses")
    ResponseEntity<Page<CourseDTO>> getUserCourses(
            @PathVariable UUID userId,
            @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUser(userId, pageable));
    }

    @PostMapping("/users/{userId}/courses/subscription")
    ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable UUID userId,
                                                        @RequestBody @Valid UserCourseDTO userCourseDTO) {

        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if(userModelOptional.get().getUserStatus().equals(UserStatus.BLOCKED)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User id blocked.");
        }

        if(userCourseService.existsByUserAndCourseId(userModelOptional.get(), userCourseDTO.getCourseId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists.");
        }

        var userModel = userModelOptional.get();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userCourseService.save(userModel.convertToUserCourseModel(userCourseDTO.getCourseId())));
    }

    @DeleteMapping("/users/courses/{courseId}")
    ResponseEntity<Object> deleteUserCourseByCourse(@PathVariable UUID courseId) {

        if(!userCourseService.existsByCourseId(courseId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists.");
        }

        userCourseService.deleteUserCourseByCourse(courseId);

        return ResponseEntity.status(HttpStatus.OK).body("UserCourse deleted successfully.");
    }

}
