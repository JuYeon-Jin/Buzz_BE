package com.buzz.community.service.post;

import com.buzz.community.dao.post.CommunityDAO;
import com.buzz.community.dto.file.FileMetaInsertDTO;
import com.buzz.community.exception.post.DuplicateFileNameException;
import com.buzz.community.exception.post.FileCountLimitExceededException;
import com.buzz.community.exception.post.FileSizeLimitExceededException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;


@Service
public class FileService {

    private final String uploadDir = "src/main/resources/upload/files/";
    private final String uniqueKey = "-";

    private CommunityDAO dao;

    public FileService(CommunityDAO dao) {
        this.dao = dao;
    }



    /**
     * 각 파일의 메타데이터를 저장한 뒤, 서버에 파일을 저장합니다.
     * 파일의 이름이 중복되거나, 용량과 갯수 한도를 초과할 시 예외가 발생하며,
     * 업로드 실패 시 DB 에서 Rollback 을 수행하고, 서버에 이미 올라간 파일이 있다면 삭제합니다.
     *
     * @param files 업로드할 MultipartFile 배열
     * @param postId 파일을 첨부할 게시물의 ID
     * @throws IOException 파일 저장 중 입출력 오류 발생 시
     * @throws FileCountLimitExceededException 파일 개수가 최대 허용 개수를 초과한 경우
     * @throws DuplicateFileNameException 업로드 파일 중 중복된 파일 이름이 있는 경우
     * @throws FileSizeLimitExceededException 파일 크기가 최대 허용 크기를 초과한 경우
     */
    public void uploadFiles(MultipartFile[] files, int postId) throws IOException {

        final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB
        final int MAX_FILE_COUNT = 5;

        if (files.length > MAX_FILE_COUNT) {
            throw new FileCountLimitExceededException("파일은 최대 " + MAX_FILE_COUNT + "개까지만 업로드 가능합니다.");
        }

        // TODO Set, HashSet 공부 필요
        Set<String> fileNamesSet = new HashSet<>();

        try {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = file.getOriginalFilename();

                    if (!fileNamesSet.add(fileName)) {
                        throw new DuplicateFileNameException("중복된 파일명(" + fileName + ")은 허용되지 않습니다.");
                    }

                    if (file.getSize() > MAX_FILE_SIZE) {
                        throw new FileSizeLimitExceededException("크기가 2MB 를 초과하는 파일이 있습니다.");
                    }

                    long fileSize = file.getSize();
                    String contentType = file.getContentType();
                    String filePathStr = uploadDir + postId + uniqueKey + fileName;

                    FileMetaInsertDTO metaDto = new FileMetaInsertDTO(postId, fileName, fileSize, contentType, filePathStr);
                    dao.insertFileMetadata(metaDto);

                    Path filePath = Paths.get(filePathStr);
                    Path dirPath = filePath.getParent();
                    if (!Files.exists(dirPath)) {
                        Files.createDirectories(dirPath);
                    }

                    Files.write(filePath, file.getBytes());
                }
            }

        } catch (DuplicateFileNameException | FileSizeLimitExceededException e) {
            deleteFiles(postId);
            throw e;
        }

    }

    // ## DELETE FILE 메타데이터 삭제
    public void deleteFiles (int postId) throws IOException {
        String filePrefix = postId + uniqueKey;
        DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(uploadDir), filePrefix + "*");
        for (Path file : stream) {
            Files.deleteIfExists(file);
        }
    }
}
