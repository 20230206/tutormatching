package com.sparta.soomtut.location.service;

import com.sparta.soomtut.location.dto.request.LocationRequest;
import com.sparta.soomtut.location.dto.response.LocationResponse;
import com.sparta.soomtut.location.entity.Location;

import java.util.List;

public interface LocationService {
    Location saveLocation(LocationRequest request);
    Location saveLocation(Location location);

    Location updateLocation(Long locationId, LocationRequest request);

    Location getLocationById(Long memberId);
    List<LocationResponse> getAllLocation(Location myLocation);
    
}
