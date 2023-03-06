package com.sparta.soomtut.chat.dto;

import com.sparta.soomtut.chat.entity.ChatRoom;
import com.sparta.soomtut.lecture.dto.response.LectureResponseDto;
import com.sparta.soomtut.lectureRequest.entity.LectureRequest;
import com.sparta.soomtut.lectureRequest.entity.LectureState;
import com.sparta.soomtut.member.dto.response.MemberResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatRoomResponse {

    private Long id;
    private MemberResponse tutee;
    private MemberResponse tutor;
    private LectureResponseDto lecture;
    private Long lecreqId;
    private LectureState state;
    private boolean reviewed;

    private LocalDateTime createdAt;

    private ChatRoomResponse(
            Long id,
            MemberResponse tutee,
            MemberResponse tutor,
            LocalDateTime createdAt,
            LectureRequest lecreq,
            LectureResponseDto lecture
    ) {
        this.id = id;
        this.tutee = tutee;
        this.tutor = tutor;
        this.createdAt = createdAt;
        this.lecreqId = lecreq.getId();
        this.state = lecreq.getLectureState();
        this.lecture = lecture;
        this.reviewed = lecreq.getReviewFilter();
    }

    public static ChatRoomResponse of(
            ChatRoom chatRoom,
            MemberResponse tutee,
            MemberResponse tutor,
            LectureResponseDto lecture,
            LectureRequest lecreq
    ) {
        return new ChatRoomResponse(
                chatRoom.getId(),
                tutee,
                tutor,
                chatRoom.getCreatedAt(),
                lecreq,
                lecture
        );
    }

}
