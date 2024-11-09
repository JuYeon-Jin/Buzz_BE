package com.buzz.community.controller.post;

import com.buzz.community.dto.post.community.PostFilterDTO;
import com.buzz.community.service.post.SupportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/support")
public class SupportController {

    private SupportService supportService;

    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }



    @GetMapping("/category")
    public ResponseEntity<?> getCategory() {
        return ResponseEntity.ok(true);
    }



    @GetMapping()
    public ResponseEntity<?> getPostList(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
                                         PostFilterDTO dto) {
        return null;
    }



    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostArticle(@PathVariable int postId) {
        return null;
    }



    @PostMapping()
    public ResponseEntity<?> insertPost() {
        return null;
    }



    @PutMapping()
    public ResponseEntity<?> updatePost() {
        return null;
    }



    @DeleteMapping()
    public ResponseEntity<?> deletePost() {
        return null;
    }


}
