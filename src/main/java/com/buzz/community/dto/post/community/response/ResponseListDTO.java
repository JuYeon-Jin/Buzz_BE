package com.buzz.community.dto.post.community.response;

import com.buzz.community.dto.post.PaginationDTO;
import com.buzz.community.dto.post.community.CommunityListDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ResponseListDTO {
    private List<CommunityListDTO> postList;
    private PaginationDTO pagination;
}
