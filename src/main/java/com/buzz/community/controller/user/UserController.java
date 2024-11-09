package com.buzz.community.controller.user;

import com.buzz.community.dto.user.req.RequestJoinDTO;
import com.buzz.community.dto.user.req.RequestLoginDTO;
import com.buzz.community.dto.user.req.duplicateIdDTO;
import com.buzz.community.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody RequestJoinDTO dto) {
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + userService.join(dto))
                .body(true);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLoginDTO dto) {
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + userService.login(dto))
                .body(true);
    }

    @PostMapping("/duplicate-id")
    public ResponseEntity<?> checkIdDuplicate(@RequestBody duplicateIdDTO dto) {
        return ResponseEntity.ok(userService.checkIdDuplicate(dto.getUsername()));
    }

}
