package com.buzz.community.dto.post.community;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityListDTO {
    private int postId;
    private String title;
    private int views;
    private String name;
    private String categoryName;
    private boolean isDeleted;
    private String createdAt;
    private boolean fileExist;
    private int commentCount;
}
