package com.buzz.community.dto.post.community;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentInsertDTO {
    private int postId;
    private String content;
    private String userId;
}
