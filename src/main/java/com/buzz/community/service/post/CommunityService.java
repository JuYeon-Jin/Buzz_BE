package com.buzz.community.service.post;

import com.buzz.community.dao.post.CommunityDAO;
import com.buzz.community.dto.file.FileMetaListDTO;
import com.buzz.community.dto.post.ContentOwnerDTO;
import com.buzz.community.dto.post.community.*;
import com.buzz.community.dto.post.CategoryDTO;
import com.buzz.community.dto.post.PaginationDTO;
import com.buzz.community.dto.post.community.response.ResponseDetailDTO;
import com.buzz.community.dto.post.community.response.ResponseListDTO;
import com.buzz.community.exception.post.DuplicateFileNameException;
import com.buzz.community.exception.post.NotAllowedException;
import com.buzz.community.exception.user.InvalidInputException;
import com.buzz.community.utils.PaginationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CommunityService {

    private CommunityDAO dao;
    private PaginationUtils pagination;
    private FileService fileService;

    public CommunityService(CommunityDAO dao, PaginationUtils pagination, FileService fileService) {
        this.dao = dao;
        this.pagination = pagination;
        this.fileService = fileService;
    }


    /**
     * 카테고리 목록을 반환합니다.
     *
     * @return 카테고리 정보를 담고 있는 List<CategoryDTO>
     */
    public List<CategoryDTO> getCategoryList() {
        return dao.selectCategoryList();
    }



    /**
     * 주어진 필터 조건과 페이지 번호에 따라 게시물 목록과 페이지네이션 정보를 반환합니다.
     *
     * @param dto 게시물 필터 조건을 담고 있는 PostFilterDTO 객체 (날짜, 카테고리, 검색어 포함)
     * @param page 현재 페이지 번호
     * @return 게시물 목록과 페이지네이션 정보를 포함하는 ResponseListDTO
     */
    public ResponseListDTO getPostList(PostFilterDTO dto, int page) {
        dto.setOffset(page, dto.getLimit());

        List<CommunityListDTO> postList =  dao.selectPostList(dto);
        PaginationDTO test = pagination.pagination(dto.getLimit(), page, dao.countTotalPost(dto));

        return new ResponseListDTO(
                dao.selectPostList(dto),
                pagination.pagination(dto.getLimit(), page, dao.countTotalPost(dto))
        );
    }



    /**
     * 지정된 게시물 ID에 해당하는 게시물의 상세 정보를 조회합니다.
     *
     * @param postId 게시물의 ID입니다.
     * @return 게시물의 상세 정보를 포함한 CommunityDetailDTO 객체를 반환합니다.
     */
    public ResponseDetailDTO getPostArticle(int postId, String userId) {
        ContentOwnerDTO dto = new ContentOwnerDTO(postId, userId);
        CommunityDetailDTO postDto = dao.selectPostById(dto);
        // TODO 조회수 2 씩 올라가는 문제 존재 함. 아마도 useEffect 때문인 듯.
        if (!postDto.isOwner()) {
            dao.incrementViewCount(postId);
        }
        return new ResponseDetailDTO(postDto, dao.selectFileMetadata(postId), dao.selectComment(dto));
    }



    /**
     * 지정된 게시물 ID에 대한 파일 메타데이터 목록을 조회합니다.
     *
     * @param postId 게시물의 ID입니다.
     * @return 파일 메타데이터 정보를 포함하는 List<FileMetaListDTO> 객체를 반환합니다.
     */
    public List<FileMetaListDTO> getFileData(int postId) {
        return dao.selectFileMetadata(postId);
    }



    /**
     * 새 게시물을 작성하고 첨부파일이 있는 경우 업로드합니다.
     *
     * @param dto 게시물 정보를 담고 있는 PostInsertDTO 객체입니다.
     * @param files 업로드할 MultipartFile 배열입니다.
     * @throws IOException 파일 업로드 중 오류가 발생한 경우 던집니다.
     * @throws InvalidInputException 게시물 기준에 부합하지 않은 경우 던집니다.
     * @throws DuplicateFileNameException 중복 파일명 오류가 발생한 경우 던집니다.
     */
    @Transactional(rollbackFor = {IOException.class, InvalidInputException.class, DuplicateFileNameException.class})
    public void insertPost(PostInsertDTO dto, MultipartFile[] files) throws IOException {

        if (dto.getCategoryId() == 0) {
            throw new InvalidInputException("카테고리를 선택해 주세요.");
        }
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("제목은 비어 있을 수 없습니다.");
        }
        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new InvalidInputException("내용은 비어 있을 수 없습니다.");

        } else if (dto.getContent().getBytes("UTF-8").length > 50000) {
            throw new InvalidInputException("내용이 입력할 수 있는 길이를 초과하였습니다.");
        }

        dao.insertPost(dto);
        if (files != null && files.length > 0) {
            fileService.uploadFiles(files, dto.getPostId());
        }
    }


    // DELETE 게시물 삭제
    public void deletePost(int postId, String userId) throws IOException {
        if (dao.hasComments(postId)) {
            throw new NotAllowedException("댓글이 작성된 게시물은 삭제할 수 없습니다.");
        }

        fileService.deleteFiles(postId);
        dao.deletePost(new ContentOwnerDTO(postId, userId));
    }


    // 댓글 추가하기
    public List<CommentListDTO> insertComment(CommentInsertDTO dto) {
        dao.insertComment(dto);
        return dao.selectComment(new ContentOwnerDTO(dto.getPostId(), dto.getUserId()));
    }

    // 댓글 삭제하기
    public List<CommentListDTO> deleteComment(int postId, int commentId, String userId) {
        dao.deleteComment(commentId);
        return dao.selectComment(new ContentOwnerDTO(postId, userId));
    }


    // UPDATE 게시물 수정

}
