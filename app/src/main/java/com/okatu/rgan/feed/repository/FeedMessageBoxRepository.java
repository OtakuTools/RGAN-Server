package com.okatu.rgan.feed.repository;

import com.okatu.rgan.feed.model.entity.FeedMessageBoxItem;
import com.okatu.rgan.user.model.RganUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedMessageBoxRepository extends JpaRepository<FeedMessageBoxItem, Long> {
    Page<FeedMessageBoxItem> findByReceiverAndMessageTypeOrderByFeedMessageDesc(RganUser user, Integer messageType, Pageable pageable);
}
