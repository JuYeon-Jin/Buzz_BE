package com.buzz.community.dto.post.community;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDetailDTO {
    private int postId;
    private String categoryName;
    private String name;
    private String title;
    private String content;
    private int views;
    private String createdAt;

    private String userId;
    private boolean isOwner;
}
