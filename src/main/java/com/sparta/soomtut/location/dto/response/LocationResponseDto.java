package com.sparta.soomtut.location.dto.response;

import com.sparta.soomtut.location.entity.Location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponseDto {
    private String address;
    private float posX;
    private float posY;
    private String sido;
    private String sigungu;
    private String bname;
    
    @Builder(builderClassName="LocationResponseToDto", builderMethodName="toDto")
    public LocationResponseDto(Location location) {
        this.address = location.getAddress();
        this.posX = location.getPosX();
        this.posY = location.getPosY();
        this.sido = location.getSido();
        this.sigungu = location.getSigungu();
        this.bname = location.getBname();
    }

}