package com.diyiliu.web.controller;

import com.diyiliu.support.config.PictureUploadProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Locale;

/**
 * Description: PictureUploadController
 * Author: DIYILIU
 * Update: 2017-10-26 10:32
 */

@Controller
@SessionAttributes("picturePath")
public class PictureUploadController {
    // public static final Resource PICTURES_DIR = new FileSystemResource("./picture");

    private final Resource picturesDir;
    private final Resource anonymousPicture;
    private final MessageSource messageSource;

    @Autowired
    public PictureUploadController(PictureUploadProperties uploadProperties, MessageSource messageSource) {
        picturesDir = uploadProperties.getUploadPath();
        anonymousPicture = uploadProperties.getAnonymousPicture();
        this.messageSource = messageSource;
    }

    @ModelAttribute("picturePath")
    public Resource picturePath() {
        return anonymousPicture;
    }

    @RequestMapping("upload")
    public String uploadPage() {

        return "profile/uploadPage";
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String onUpload(MultipartFile file, Model model,
                           RedirectAttributes redirectAttributes) throws IOException {
        if (file.isEmpty() || !isImage(file)) {
            redirectAttributes.addFlashAttribute("error", "Incorrect file. Please upload a picture.");

            return "redirect:/upload";
        }

        Resource picturePath = copyFileToPictures(file);
        model.addAttribute("picturePath", picturePath);

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

    /*@RequestMapping(value = "uploadedPicture")
    public void getUploadedPicture(HttpServletResponse response) throws IOException {
        *//*
        ClassPathResource classPathResource = new ClassPathResource("/image/user.jpg");
        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(classPathResource.getFilename()));
        FileCopyUtils.copy(classPathResource.getInputStream(), response.getOutputStream());
        *//*

        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(anonymousPicture.getFilename()));
        FileCopyUtils.copy(anonymousPicture.getInputStream(), response.getOutputStream());
    }*/

    @RequestMapping(value = "uploadedPicture")
    public void getUploadedPicture(HttpServletResponse response,
                                   @ModelAttribute("picturePath") Resource picturePath) throws IOException {
        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(picturePath.getFilename()));
        FileCopyUtils.copy(picturePath.getInputStream(), response.getOutputStream());
    }

    /**
     * 异常错误跳转
     *
     * @return
     */
    /*@RequestMapping("uploadError")
    public ModelAndView onUploadError(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("profile/uploadPage");
        modelAndView.addObject("error", request.getAttribute(WebUtils.ERROR_MESSAGE_ATTRIBUTE));
        return modelAndView;
    }*/
    @RequestMapping("uploadError")
    public ModelAndView onUploadError(Locale locale) {
        ModelAndView modelAndView = new ModelAndView("profile/uploadPage");
        modelAndView.addObject("error", messageSource.getMessage("upload.file.too.big", null, locale));
        return modelAndView;
    }


    /**
     * 异常处理类
     *
     * @param locale
     * @return
     */
    @ExceptionHandler(IOException.class)
    public ModelAndView handleIOException(Locale locale) {
        ModelAndView modelAndView = new ModelAndView("profile/ uploadPage");
        modelAndView.addObject("error", messageSource.getMessage("upload.io.exception", null, locale));
        return modelAndView;
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
