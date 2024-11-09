package com.buzz.community.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaginationDTO {
    private int startListIndex;
    private int endListIndex;
    private int maxListIndex;
    private int currentPage;
    private int totalPostNumber;
}
