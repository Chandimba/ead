package com.ead.authuser.controllers;

import com.ead.authuser.dto.InstructorDTO;
import com.ead.authuser.enums.RoleType;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.RoleModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.RoleService;
import com.ead.authuser.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@RestControllerAdvice
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("instructors")
public class InstructorController {

    private final UserService userService;
    private final RoleService roleService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/subscription")
    ResponseEntity<Object> saveSubscriptionInstructor(@RequestBody @Valid InstructorDTO instructorDTO){
        Optional<UserModel> userModelOptional = userService.findById(instructorDTO.getUserId());

        if(userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        RoleModel roleModel = roleService.findByRoleName(RoleType.ROLE_INSTRUCTOR)
                .orElseThrow(() -> new RuntimeException("Role is not found."));

        UserModel userModel = userModelOptional.get();
        userModel.setUserType(UserType.INSTRUCTOR);
        userModel.getRoles().add(roleModel);
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userService.updateUser(userModel);

        return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }

}
