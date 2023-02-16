package com.sparta.soomtut.repository;

import com.sparta.soomtut.entity.TuitionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TuitionRequestRepository extends JpaRepository<TuitionRequest,Long> {

    Optional<TuitionRequest> findByPostIdAndTuteeId(Long postId, Long tuteeId);
}