package com.sparta.soomtut.image.controller;

import java.io.IOException;
import java.util.List;

import com.sparta.soomtut.member.service.MemberService;
import com.sparta.soomtut.util.security.UserDetailsImpl;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.soomtut.image.dto.request.ImageRequest;
import com.sparta.soomtut.image.dto.response.ImageResponse;
import com.sparta.soomtut.image.service.ImageService;
import com.sparta.soomtut.image.service.S3Service;

import org.springframework.http.ResponseEntity;

import com.sparta.soomtut.util.response.SuccessCode;
import com.sparta.soomtut.util.response.ToResponse;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final S3Service s3Service;

    //마이페이지 이미지 업로드
    @PostMapping(value = "/member")
    public ResponseEntity<?> profileImage(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam("file") MultipartFile file) throws IOException
    {
        String imgPath = s3Service.uploadProfile(userDetails.getMemberId(), file);
        imageService.saveImgPost(imgPath);
        return ToResponse.of(null, SuccessCode.IMG_PROFILE_OK);
    }

//    @DeleteMapping(value = "member/{memberid}")
//    public String delProfile(@PathVariable Long memberid, @ModelAttribute ImageRequest imageDto){
//        s3Service.delProfile(imageDto);
//        return "Del Su";
//    }

    //마이페이지 이미지 조회
    @GetMapping(value = "/member/{memberid}")
    public String  getImage(Model model){
        List<ImageResponse> imageDtoList = imageService.getList();

        model.addAttribute("imageList", imageDtoList);

        return imageDtoList.toString();
    }

    // 수업글 이미지 업로드
    // @PostMapping("/lecture/{lectureid}")
    // public String  postImage(
    //         MultipartFile file) throws IOException
    // {
    //     String imgPath = s3Service.uploadPostImage(imageDto.getFilePath(), file);
    //     imageDto.setFilePath(imgPath);
    //     imageService.saveImgPost(imageDto);
    //     return "redirect:/lectureimages";
    // }
    // 수업글 이미지 조회
    @GetMapping("/lecture/{lectureid}")
    public String getPostImage(Model model){
        List<ImageResponse> imageDtoList = imageService.getList();

        model.addAttribute("imageList", imageDtoList);

        return "/lectureimages";
    }

}
