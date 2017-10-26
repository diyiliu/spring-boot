package com.diyiliu.web.controller;

import com.diyiliu.support.config.PictureUploadProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

/**
 * Description: PictureUploadController
 * Author: DIYILIU
 * Update: 2017-10-26 10:32
 */

@Controller
public class PictureUploadController {
    // public static final Resource PICTURES_DIR = new FileSystemResource("./picture");

    private final Resource picturesDir;
    private final Resource anonymousPicture;

    @Autowired
    public PictureUploadController(PictureUploadProperties uploadProperties) {
        picturesDir = uploadProperties.getUploadPath();
        anonymousPicture = uploadProperties.getAnonymousPicture();
    }

    @RequestMapping("upload")
    public String uploadPage() {

        return "profile/uploadPage";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String onUpload(MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {

        if (file.isEmpty() || !isImage(file)) {
            redirectAttributes.addFlashAttribute("error", "Incorrect file. Please upload a picture.");

            return "redirect:/upload";
        }

        copyFileToPictures(file);

        return "profile/uploadPage";
    }

    private Resource copyFileToPictures(MultipartFile file) throws IOException {
        String fileExtension = getFileExtension(file.getOriginalFilename());

        // 创建临时文件，自动生成随机数在文件名中
        // File tempFile = File.createTempFile("pic", fileExtension, PICTURES_DIR.getFile());
        File tempFile = File.createTempFile("pic", fileExtension, picturesDir.getFile());

        /*
        // try...with 代码块将会自动关闭流
        try (InputStream in = file.getInputStream();
             OutputStream out = new FileOutputStream(tempFile)) {

            IOUtils.copy(in, out);
        }
        */
        FileCopyUtils.copy(file.getBytes(), tempFile);

        return new FileSystemResource(tempFile);
    }

    @RequestMapping(value = "/uploadedPicture")
    public void getUploadedPicture(HttpServletResponse response) throws IOException {
        /*
        ClassPathResource classPathResource = new ClassPathResource("/image/user.jpg");
        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(classPathResource.getFilename()));
        FileCopyUtils.copy(classPathResource.getInputStream(), response.getOutputStream());
        */

        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(anonymousPicture.getFilename()));
        FileCopyUtils.copy(anonymousPicture.getInputStream(), response.getOutputStream());
    }

    /**
     * getContentType() 方法将会返回文件的多用途Internet 邮件扩展
     * （Multipurpose Internet Mail Extensions，MIME）类型。
     * 它将会是image/png、image/jpg 等。
     * 所以，只需要检查MIME 类型是否以“image”开头即可。
     *
     * @param file
     * @return
     */
    private boolean isImage(MultipartFile file) {

        return file.getContentType().startsWith("image");
    }

    /**
     * 获取文件后缀
     * .jpg .png
     *
     * @param name
     * @return
     */
    private static String getFileExtension(String name) {

        return name.substring(name.lastIndexOf("."));
    }
}