package com.buzz.community.utils;

import com.buzz.community.dto.post.PaginationDTO;
import org.springframework.stereotype.Component;

@Component
public class PaginationUtils {

    /**
     * 게시물 페이지관련 정보를 계산하고 반환합니다.
     * 주어진 검색 조건을 반영한 전체 게시물 수를 계산하고, 페이지네이션 정보를 포함한 PostStatisticsDTO 를 생성합니다.
     *
     * @param postLimit   검색 조건을 포함한 DTO 입니다.
     * @param currentPage 현재 페이지 번호입니다.
     * @return PaginationDTO 를 반환합니다.
     */
    public PaginationDTO pagination(int postLimit, int currentPage, int totalPosts) {
        int indexLimit = 10;
        int maximumIndex = ((totalPosts-1)/postLimit) + 1;

        int startIndex = ((currentPage-1)/10) * indexLimit + 1;
        int endIndex = Math.min(startIndex + (indexLimit - 1), maximumIndex);

        return new PaginationDTO(startIndex, endIndex, maximumIndex, currentPage, totalPosts);
    }

}
