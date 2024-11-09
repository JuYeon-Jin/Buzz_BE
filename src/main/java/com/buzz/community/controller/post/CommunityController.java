package com.buzz.community.controller.post;

import com.buzz.community.dto.post.community.CommentInsertDTO;
import com.buzz.community.dto.post.community.PostInsertDTO;
import com.buzz.community.dto.post.community.PostFilterDTO;
import com.buzz.community.service.post.CommunityService;
import com.buzz.community.utils.JWTUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user/board/community")
public class CommunityController {

    private CommunityService communityService;
    private JWTUtils jwt;

    public CommunityController(CommunityService communityService,
                               JWTUtils jwt) {
        this.communityService = communityService;
        this.jwt = jwt;
    }


    @GetMapping("/category")
    public ResponseEntity<?> getCategory() {
        return ResponseEntity.ok(communityService.getCategoryList());
    }


    @GetMapping()
    public ResponseEntity<?> getPostList(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
                                         PostFilterDTO dto) {
        return ResponseEntity.ok(communityService.getPostList(dto, page));
    }


    // TODO 나중에 스프링 시큐리티를 사용하면 컨트롤러 타기전에 한번에 JWT 유효성 검사를 실행 할 수 있음.
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostArticle(@PathVariable int postId,
                                            @RequestHeader(value = "Authorization", required = false) String tokenHeader) {

        String token = tokenHeader.startsWith("Bearer ") ? tokenHeader.substring(7) : null;

        if (!token.equals("null")) {
            return ResponseEntity.ok(communityService.getPostArticle(postId, jwt.getUserId(token)));
        }
        return ResponseEntity.ok(communityService.getPostArticle(postId, "guestUser"));
    }


    @PostMapping()
    public ResponseEntity<?> insertPost(@RequestPart("post") PostInsertDTO dto,
                                        @RequestPart(value = "files", required = false) MultipartFile[] files,
                                        @RequestHeader("Authorization") String tokenHeader) throws IOException {
        String token = tokenHeader.startsWith("Bearer ") ? tokenHeader.substring(7) : null;
        String newToken = jwt.validateToken(token);

        dto.setUserId(jwt.getUserId(token));
        communityService.insertPost(dto, files);

        if (newToken != null) {
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + newToken)
                    .body(true);
        }

        return ResponseEntity.ok(true);
    }



    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable int postId,
                                        @RequestHeader(value = "Authorization", required = false) String tokenHeader) throws IOException {
        String token = tokenHeader.startsWith("Bearer ") ? tokenHeader.substring(7) : null;
        String newToken = jwt.validateToken(token);
        String userId = jwt.getUserId(token);

        communityService.deletePost(postId, userId);

        if (newToken != null) {
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + newToken)
                    .body(true);
        }

        return ResponseEntity.ok(true);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> insertComment(@RequestBody CommentInsertDTO dto,
                                           @RequestHeader(value = "Authorization", required = false) String tokenHeader) {
        String token = tokenHeader.startsWith("Bearer ") ? tokenHeader.substring(7) : null;
        String newToken = jwt.validateToken(token);
        dto.setUserId(jwt.getUserId(token));

        if (newToken != null) {
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + newToken)
                    .body(communityService.insertComment(dto));
        }

        return ResponseEntity.ok(communityService.insertComment(dto));
    }


    @DeleteMapping("/{postId}/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int postId,
                                           @PathVariable int commentId,
                                           @RequestHeader(value = "Authorization", required = false) String tokenHeader) {
        String token = tokenHeader.startsWith("Bearer ") ? tokenHeader.substring(7) : null;
        String newToken = jwt.validateToken(token);

        if (newToken != null) {
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + newToken)
                    .body(communityService.deleteComment(postId, commentId, jwt.getUserId(token)));
        }

        return ResponseEntity.ok(communityService.deleteComment(postId, commentId, jwt.getUserId(token)));
    }




    @PutMapping()
    public ResponseEntity<?> updatePost() {
        return null;
    }

}
