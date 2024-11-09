package com.buzz.community.controller.post;

import com.buzz.community.utils.PhotoUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/user/community")
public class CkeditorController {

    private final PhotoUtils photoUtils;

    public CkeditorController(PhotoUtils photoUtils) {
        this.photoUtils = photoUtils;
    }

    @PostMapping("/ck-edit/upload")
    public ModelAndView upload(@RequestParam("upload") MultipartFile file) {
        ModelAndView mav = new ModelAndView("jsonView");

        // String uploadPath = photoUtils.saveFile(file);

        mav.addObject("uploaded", true);
        // mav.addObject("url", uploadPath);

        return mav;
    }

}
