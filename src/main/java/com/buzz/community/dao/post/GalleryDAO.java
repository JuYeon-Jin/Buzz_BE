package com.buzz.community.dao.post;

import com.buzz.community.dto.post.CategoryDTO;

import java.util.List;

public interface GalleryDAO {

    // SELECT 카테고리 List
    List<CategoryDTO> selectCategoryList();
}
