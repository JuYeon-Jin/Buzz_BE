package com.buzz.community.dto.post.community.response;

import com.buzz.community.dto.file.FileMetaListDTO;
import com.buzz.community.dto.post.community.CommentListDTO;
import com.buzz.community.dto.post.community.CommunityDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ResponseDetailDTO {
    private CommunityDetailDTO postArticle;
    private List<FileMetaListDTO> fileMetaList;
    private List<CommentListDTO> comment;
}
