package com.sparta.soomtut.lecture.service.impl;

import com.sparta.soomtut.lecture.dto.request.CreateLectureRequestDto;
import com.sparta.soomtut.lecture.dto.request.UpdateLectureRequestDto;
import com.sparta.soomtut.lecture.dto.response.LectureResponseDto;
import com.sparta.soomtut.lecture.entity.Category;
import com.sparta.soomtut.lecture.entity.Lecture;
import com.sparta.soomtut.lecture.repository.LectureRepository;
import com.sparta.soomtut.lecture.service.LectureService;
import com.sparta.soomtut.lectureRequest.entity.LectureRequest;
import com.sparta.soomtut.lectureRequest.repository.LectureRequestRepository;
import com.sparta.soomtut.member.entity.Member;
import com.sparta.soomtut.member.entity.enums.MemberRole;
import com.sparta.soomtut.util.enums.LectureState;

import com.sparta.soomtut.util.response.ErrorCode;
import com.sparta.soomtut.location.service.LocationService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final LocationService locationService;
    private final LectureRequestRepository lectureRequestRepository;

    @Override
    @Transactional
    public LectureResponseDto getLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(
            () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage()));
        return new LectureResponseDto(lecture, locationService.findMemberLocation(lecture.getTutorId()));
    }

    // 게시글 작성
    @Override
    @Transactional
    public LectureResponseDto createLecture(Member member, CreateLectureRequestDto lectureRequestDto) {
        Lecture lecture = new Lecture(lectureRequestDto, member);
        lectureRepository.save(lecture);
        return new LectureResponseDto(lecture, locationService.findMemberLocation(member.getId()));
    }

    // 게시글 수정
    @Override
    @Transactional
    public LectureResponseDto updateLecture(Long lectureId, UpdateLectureRequestDto lectureRequestDto, Member member) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage())
        );

        // 작성자 또는 관리자만 수정가능
        if (member.getMemberRole() != MemberRole.ADMIN) {
            if (!lecture.getTutorId().equals(member.getId()))
                throw new IllegalArgumentException(ErrorCode.AUTHORIZATION.getMessage());
        }

        lecture.update(lectureRequestDto);
        return new LectureResponseDto(lecture);
    }

    //게시글 삭제
    @Override
    @Transactional
    public void deleteLecture(Long lectureId, Member member) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage())
        );

        if (member.getMemberRole() == MemberRole.ADMIN)
            lectureRepository.delete(lecture);

        lectureRepository.deleteById(lectureId);
    }


    @Override
    @Transactional
    public Lecture getLectureById(Long lectureId){
       Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_CLASS.getMessage())
        );

        return lecture;
    }

    @Override
    @Transactional
    public Long getTutorId(Long lectureId) {
        return getLectureById(lectureId).getTutorId();
    }

    // 이상한데, 멤버 Id 로 수업을 찾아오면 수업이 여러개 있을 수 있는거 아닌가?
    @Override
    public LectureResponseDto getMyLecture(Member member) {
        Lecture lecture = lectureRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_CLASS.getMessage()));
        LectureResponseDto lectureResponseDto = 
                new LectureResponseDto(lecture, member.getNickname(), locationService.findMemberLocation(member.getId()).getAddress());
        return lectureResponseDto;
    }

    // 수업 신청
    @Override
    @Transactional
    public String createLectureRequest(Long lectureid, Member member) {
        Lecture lecture = lectureRepository.findById(lectureid).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage())
        );

//        boolean isExistsRequest = LectureRequestRepository.existsByLectureIdAndTuteeIdAndLectureState(lectureid, member.getId(), LectureState.NONE);
//        if(isExistsRequest) return "수업 신청이 완료되었습니다.";

        LectureRequest tuitionRequest = new LectureRequest(lecture, member.getId());

        lectureRequestRepository.save(tuitionRequest);
        return "수업 확정이 완료되었습니다.";
    }



    //수업 확정
    @Override
    @Transactional
    public String classConfirmed(Long lecturerequestid, Member member) {
        LectureRequest lectureRequest = lectureRequestRepository.findById(lecturerequestid).orElseThrow(
                () -> new IllegalArgumentException("ConfirmedError")
        );
        lectureRequest.changeConfirmed();
        return "수업이 확정되었습니다.";

    }



    //수업 완료
    @Override
    @Transactional
    public String classComplete(Long lecturerequestid, Member member) {

        LectureRequest tuitionRequest = lectureRequestRepository.findById(lecturerequestid).orElseThrow(
                () -> new IllegalArgumentException("Error")
        );

        tuitionRequest.changeComplete();
        return "수업이 완료되었습니다.";
    }


    // 완료한 수업 목록 조회
    @Override
    @Transactional
    public List<Lecture> getCompleteLecture(Member member) {
        List<LectureRequest> lectureRequestList = lectureRequestRepository.findAllByTuteeIdAndLectureState(member.getId(), LectureState.DONE);
        List<Lecture> postList = lectureRequestList.stream().map((item) -> item.getLecture()).collect(Collectors.toList());
        return postList;
    }


    // 완료된 수업중 리뷰가 없는 수업목록 조회
    @Override
    @Transactional
    public List<Lecture> reviewFilter(Member member) {
        List<LectureRequest> lectureRequestList = lectureRequestRepository.findAllByTuteeIdAndLectureStateAndReviewFilterIsFalse(member.getId(), LectureState.DONE);
        List<Lecture> lectureList = lectureRequestList.stream().map((item) -> item.getLecture()).collect(Collectors.toList());
        return lectureList;
    }



    @Override
    @Transactional(readOnly = true) 
    public boolean checkLectureAuthor(Long lectureId, Member member)
    {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(
            () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_CLASS.getMessage())
        );

        return lecture.getTutorId().equals(member.getId());
    }

    @Override
    @Transactional(readOnly = true) 
    public Page<Lecture> getLectures(int category, Pageable pageable){
        return lectureRepository.findAllByCategory(Category.valueOf(category), pageable);
    }

    @Override
    @Transactional(readOnly = true) 
    public Page<Lecture> getLectures(Pageable pageable) {
        return lectureRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true) 
    public Page<Lecture> getAllLectureByMemberId(Long memberId, Pageable pageable) {
        return lectureRepository.findAllByMemberId(memberId, pageable);
    }

    @Override
    @Transactional
    public Page<LectureResponseDto> searchByKeyword(String keyword,Pageable pageable) {
        return lectureRepository.findLectureByKeyword(keyword,pageable);

    }

}
