package com.sparta.soomtut.chat.service;

import com.sparta.soomtut.chat.dto.ChatRequestDto;
import com.sparta.soomtut.chat.dto.ChatResponseDto;
import com.sparta.soomtut.chat.entity.ChatMessage;
import com.sparta.soomtut.chat.repository.ChatMessageRepository;
import com.sparta.soomtut.util.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{

    private final ChatMessageRepository chatMessageRepository;

    // 채팅 메시지(1개) 저장.
    public void save(ChatRequestDto chatRequest) {
        chatMessageRepository.save(ChatMessage.of(chatRequest));
    }

    // 가장 최근 대화 내용 한 개 가져오기 - exception 수정 필
    @Override
    public ChatResponseDto getLastChatMessage(Long roomId) {
         ChatMessage chatMessage = chatMessageRepository.findByRoomIdOrderBySentAtDesc(roomId)
                 .orElseThrow(()->new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage()));
         return ChatResponseDto.of(chatMessage);
    }

    // 대화 내용 전부 가져오기 - 수정 필요 멤버 아이디로 찾아야함.
    @Override
    public List<ChatResponseDto> getAllChatMessages(Long roomId) {

        List<ChatMessage> chatMessages = chatMessageRepository.findByRoomId(roomId);
        List<ChatResponseDto> chatRequestDtoList = new ArrayList<>();
        chatMessages.forEach(chat -> chatRequestDtoList.add(ChatResponseDto.of(chat)));

        return chatRequestDtoList;
    }

}