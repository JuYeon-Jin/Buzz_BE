package com.buzz.community.dao.post;

import com.buzz.community.dto.file.FileMetaDTO;
import com.buzz.community.dto.file.FileMetaInsertDTO;
import com.buzz.community.dto.file.FileMetaListDTO;
import com.buzz.community.dto.post.ContentOwnerDTO;
import com.buzz.community.dto.post.community.*;
import com.buzz.community.dto.post.CategoryDTO;

import java.util.List;

public interface CommunityDAO {

    // SELECT 카테고리 List
    List<CategoryDTO> selectCategoryList();

    // SELECT 게시물 TOTAL COUNT
    int countTotalPost(PostFilterDTO dto);

    // SELECT 게시물 List
    List<CommunityListDTO> selectPostList(PostFilterDTO dto);

    // SELECT 게시물 Article
    CommunityDetailDTO selectPostById(ContentOwnerDTO dto);

    // 조회수 증가
    void incrementViewCount(int postId);

    // INSERT 게시물 작성
    void insertPost(PostInsertDTO dto);

    // UPDATE 게시물 수정
    // void updatePost();

    // DELETE 게시물 삭제
    void deletePost(ContentOwnerDTO dto);

    // ## SELECT FILE 메타데이터 List
    List<FileMetaListDTO> selectFileMetadata(int postId);

    // ## SELECT FILE 메타데이터
    FileMetaDTO fileDataForDownload(int fileId);

    // ## INSERT FILE 메타데이터 입력
    void insertFileMetadata(FileMetaInsertDTO fileMetaInsertDTO);

    // ## SELECT 댓글 List
    List<CommentListDTO> selectComment(ContentOwnerDTO dto);

    // ## INSERT 댓글 작성
    void insertComment(CommentInsertDTO dto);

    // ## DELETE 댓글 삭제
    void deleteComment(int commentId);

    //## 댓글의 존재 유무 확인
    boolean hasComments(int postId);



}
