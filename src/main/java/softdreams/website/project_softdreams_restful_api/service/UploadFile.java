package softdreams.website.project_softdreams_restful_api.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import softdreams.website.project_softdreams_restful_api.dto.response.UploadFileRes;

@Service
public class UploadFile {
    @Value("${upload-file.base-uri}")
    private String baseUri;

    public void createDirectory(String folder) throws URISyntaxException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if (!tmpDir.isDirectory()) {
            try {
                Files.createDirectory(tmpDir.toPath());
                System.out.println(">>> CREATE NEW DIRECTORY SUCCESSFUL, PATH = " + tmpDir.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(">>> SKIP MAKING DIRECTORY, ALREADY EXISTS");
        }
    }

    public String storeFile(MultipartFile file, String folder) throws URISyntaxException, IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        URI uri = new URI(baseUri + folder + "/" + fileName);
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        }
        return fileName;
    }

    public UploadFileRes convertToUploadFileDTO(String fileName) {
        UploadFileRes resUploadFileDTO = new UploadFileRes(fileName, Instant.now());
        return resUploadFileDTO;
    }
}
