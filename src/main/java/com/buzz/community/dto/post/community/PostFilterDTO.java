package com.buzz.community.dto.post.community;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostFilterDTO {
    private String startDate;
    private String endDate;
    private int categoryId;
    private String keyword;

    // 정렬 DEFAULT 등록일시 / (createdAt, views)
    private String sortCriteria;

    // 정렬 DEFAULT 내림차순 / (desc, asc)
    private String sortDirection;

    // 반환할 레코드의 최대 수
    private int limit;

    // 시작할 레코드의 위치
    private int offset;

    public void setOffset(int currentPage, int limit) {
        this.limit = (limit == 0) ? 10 : limit;
        this.offset = (currentPage - 1) * this.limit;
    }
}
