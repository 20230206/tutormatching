package com.sparta.soomtut.service.impl;

import com.sparta.soomtut.dto.request.ImageRequest;
import com.sparta.soomtut.dto.response.ImageResponse;
import com.sparta.soomtut.entity.ImageEntity;
import com.sparta.soomtut.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ImageService {

    private S3Service s3Service;

    private ImageRepository imageRepository;

    public void saveImgPost(ImageRequest imageDto){
        imageRepository.save(imageDto.toEntity());
    }

    public List<ImageResponse> getList(){
        List<ImageEntity> imageEntityList = imageRepository.findAll();
        List<ImageResponse> imageDtoList = new ArrayList<>();

        for(ImageEntity imageEntity : imageEntityList){
            imageDtoList.add(convertEntityToDto(imageEntity));
        }

        return imageDtoList;
    }

    private ImageResponse convertEntityToDto(ImageEntity imageEntity){
        return ImageResponse.builder()
                .id(imageEntity.getId())
                .filePath(imageEntity.getFilePath())
                .imgFullPath("https://" + s3Service.CLOUD_FRONT_DOMAIN_NAME + "/" + imageEntity.getFilePath())
                .build();
    }
}
