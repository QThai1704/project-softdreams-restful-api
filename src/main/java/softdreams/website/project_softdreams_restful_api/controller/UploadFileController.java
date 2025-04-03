package softdreams.website.project_softdreams_restful_api.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import softdreams.website.project_softdreams_restful_api.dto.response.UploadFileRes;
import softdreams.website.project_softdreams_restful_api.exception.FileException;
import softdreams.website.project_softdreams_restful_api.service.UploadFile;
import softdreams.website.project_softdreams_restful_api.util.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class UploadFileController {
    @Value("${upload-file.base-uri}")
    private String baseUri;

    @Autowired
    private UploadFile fileService;
    
    @PostMapping({"/upload-file", "/admin/upload-file"})
    @ApiMessage(message = "Upload một file")
    public ResponseEntity<UploadFileRes> uploadFile(@RequestParam("folder") String folder,
            @RequestParam("file") MultipartFile file) throws URISyntaxException, IOException, FileException {
        if (file == null || file.isEmpty()) {
            throw new FileException("File is empty");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedUploadFile = List.of("png", "jpeg", "jpg", "gif", "pdf", "doc");
        boolean isValid = allowedUploadFile.stream().anyMatch(fileName::endsWith);
        if (!isValid) {
            throw new FileException("File is not valid. Only accept " + allowedUploadFile.toString());
        }
        this.fileService.createDirectory(baseUri + folder);
        return ResponseEntity.ok()
                .body(this.fileService.convertToUploadFileDTO(this.fileService.storeFile(file, folder)));
    }
}
