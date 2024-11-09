package com.buzz.community.dto.post.community;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentListDTO {
    private int commentId;
    private String name;
    private String content;
    private String createdAt;
    private boolean isOwner;
}
