package com.project.common.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class FileLocalUploaderImpl implements FileUploader{

    @Value("${file.upload.path}")
    private String fileUploadPath;
    @Value("${file.down.path}")
    private String fileDownPath;

    public String imageUpload(MultipartFile file) throws Exception {

        String current_date = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String basePath = new File("").getAbsolutePath()+fileUploadPath;

        String[] imageFileFlags = file.getOriginalFilename().split("\\.");
        String imageExt=imageFileFlags[imageFileFlags.length-1];

        String imagePath = basePath + "image" + current_date + "." + imageExt;
        String imageDownUrl = fileDownPath + "image" + current_date + "." + imageExt;

        File dest = new File(imagePath);
        file.transferTo(dest);

        return imageDownUrl;
    }
}
