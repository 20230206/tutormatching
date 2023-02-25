package com.sparta.soomtut.lectureRequest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.soomtut.util.security.UserDetailsImpl;
import com.sparta.soomtut.util.response.ToResponse;
import com.sparta.soomtut.util.response.SuccessCode;

import com.sparta.soomtut.lecture.service.LectureService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lecture-request")
@RequiredArgsConstructor
public class LectureRequestController {
    
    private final LectureService lectureService;

    // 수업 신청
    @PostMapping("/{lectureid}")
    public ResponseEntity<?> createLectureRequest(
        @PathVariable Long lectureid,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        
        return ToResponse.of(null, SuccessCode.LECTUREREQUEST_CREATE_OK);
    }

    // 수업 확정
    @PostMapping("/{lecturerequestid}/accept")
    public ResponseEntity<?> classConfirmed(
        @PathVariable Long lecturerequestid, 
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String confiremd = lectureService.classConfirmed(lecturerequestid, userDetails.getMember());
        return ToResponse.of(confiremd, SuccessCode.LECTUREREQUEST_ACCEPT_OK);
    }

    // 수업 완료
    @PutMapping("/{lecturerequestid}/complete")
    public ResponseEntity<?> classComplete(
        @PathVariable Long lecturerequestid,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String complete = lectureService.classComplete(lecturerequestid, userDetails.getMember());
        return ToResponse.of(complete, SuccessCode.LECTUREREQUEST_COMPLETE_OK);
    }

    // 수업 신청 목록 조회
    @GetMapping
    public ResponseEntity<?> getLecturesRequests(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    )
    {
        return ToResponse.of(null, SuccessCode.LECTUREREQUEST_GETREQUESTS_OK);
    }

    // 완료된 수업 목록 조회
    // TODO: 수업의 완료라기 보다는 수업 신청의 완료라고 보는 것이 타당한 것 같습니다.
    @GetMapping("/done")
    public ResponseEntity<?> getCompletePost(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        var data = lectureService.getCompletePost(userDetails.getMember());
        return ToResponse.of(data, SuccessCode.LECTURE_GETDONELECUTES_OK);
    }

}