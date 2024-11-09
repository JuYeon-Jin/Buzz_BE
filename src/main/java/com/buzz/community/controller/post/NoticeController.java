package com.buzz.community.controller.post;

import com.buzz.community.dto.post.community.PostFilterDTO;
import com.buzz.community.service.post.NoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/notice")
public class NoticeController {

    private NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
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
