package com.buzz.community.controller.post;

import com.buzz.community.dao.post.CommunityDAO;
import com.buzz.community.dto.file.FileMetaDTO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.net.URLEncoder;

@RestController
public class FileDownloadController {

    // TODO final 붙인거랑 아닌거 차이 공부
    private final CommunityDAO dao;

    public FileDownloadController(CommunityDAO dao) {
        this.dao = dao;
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable int fileId) {
        // TODO InputStreamResource 공부

        try {
            FileMetaDTO dto = dao.fileDataForDownload(fileId);
            File file = new File(dto.getFilePath());

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            // TODO 왜 파일이름을 인코딩해야하는지? 인코딩 안하면 어떻게 되는지?
            String encodedFileName = URLEncoder.encode(dto.getFileName(), "UTF-8").replace("+", "%20");

            // TODO 헤더에 왜 이 데이터들이 담겨야 하는지? 안담기면 어떻게 되는지?
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", encodedFileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .body(resource);

        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
