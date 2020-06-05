package com.okatu.rgan.feed.repository;

import com.okatu.rgan.feed.constant.FeedMessageStatus;
import com.okatu.rgan.feed.constant.FeedMessageType;
import com.okatu.rgan.feed.model.entity.FeedMessageBoxItem;
import com.okatu.rgan.user.model.RganUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FeedMessageBoxRepository extends JpaRepository<FeedMessageBoxItem, Long> {

    Page<FeedMessageBoxItem> findByReceiverAndMessageTypeAndMessageStatusAndReadIsFalseOrderByIdDesc(RganUser receiver, FeedMessageType messageType, FeedMessageStatus messageStatus, Pageable pageable);

    Page<FeedMessageBoxItem> findByReceiverAndMessageTypeAndMessageStatusOrderByIdDesc(RganUser receiver, FeedMessageType messageType, FeedMessageStatus messageStatus, Pageable pageable);

    @Query(value = "SELECT item FROM FeedMessageBoxItem item " +
        "WHERE item.receiver = ?1 AND " +
        "item.messageStatus = ?2 AND " +
        "(item.messageType=com.okatu.rgan.feed.constant.FeedMessageType.BLOG_VOTE OR item.messageType=com.okatu.rgan.feed.constant.FeedMessageType.COMMENT_VOTE)" +
        "ORDER BY item.id DESC ")
    Page<FeedMessageBoxItem> findAllVoteItemByReceiverAndMessageStatusOrderByIdDesc(RganUser receiver, FeedMessageStatus status, Pageable pageable);

    Optional<FeedMessageBoxItem> findByMessageIdAndMessageType(Long messageId, FeedMessageType messageType);

    boolean existsByReceiverAndReadIsFalse(RganUser receiver);

    List<FeedMessageBoxItem> findAllByIdInAndReceiver(Collection<Long> id, RganUser receiver);

    int countByReceiverAndMessageTypeAndMessageStatusAndReadIsFalse(RganUser receiver, FeedMessageType messageType, FeedMessageStatus messageStatus);

    @Query(value = "SELECT COUNT(item) FROM FeedMessageBoxItem item " +
        "WHERE item.receiver= ?1 AND " +
        "item.messageStatus = ?2 AND " +
        "(item.messageType=com.okatu.rgan.feed.constant.FeedMessageType.BLOG_VOTE OR item.messageType=com.okatu.rgan.feed.constant.FeedMessageType.COMMENT_VOTE)"
    )
    int countUnreadVoteItemByReceiverAndMessageStatus(RganUser receiver, FeedMessageStatus messageStatus);
}
