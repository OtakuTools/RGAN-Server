package com.okatu.rgan.feed.repository;

import com.okatu.rgan.feed.model.entity.FeedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedMessageRepository extends JpaRepository<FeedMessage, Long> {
}
