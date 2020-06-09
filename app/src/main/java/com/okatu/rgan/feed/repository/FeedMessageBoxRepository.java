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

    Page<FeedMessageBoxItem> findByReceiverAndMessageTypeAndMessageStatusOrderByCreatedTimeDesc(RganUser receiver, FeedMessageType messageType, FeedMessageStatus messageStatus, Pageable pageable);

    @Query(value = "SELECT item FROM FeedMessageBoxItem item " +
        "WHERE item.receiver = ?1 AND " +
        "item.messageStatus = ?2 AND " +
        "(item.messageType=com.okatu.rgan.feed.constant.FeedMessageType.BLOG_VOTE OR item.messageType=com.okatu.rgan.feed.constant.FeedMessageType.COMMENT_VOTE)" +
        "ORDER BY item.createdTime DESC ")
    Page<FeedMessageBoxItem> findAllVoteItemByReceiverAndMessageStatusOrderByCreatedTimeDesc(RganUser receiver, FeedMessageStatus status, Pageable pageable);
    
    // we know that only for this two kind of situation, the FeedMessageBoxItem is unique
    // since a vote item will not have two receiver
    // but for other situation, you can determine a single FeedMessageBoxItem by message_id and message_type,
    // have to provide receiver_id
    @Query(value = "SELECT item FROM FeedMessageBoxItem item " +
        "WHERE item.messageId = ?1 AND " +
        "item.messageType = com.okatu.rgan.feed.constant.FeedMessageType.BLOG_VOTE")
    Optional<FeedMessageBoxItem> findBlogVoteItemByMessageId(Long messageId);

    @Query(value = "SELECT item FROM FeedMessageBoxItem item " +
        "WHERE item.messageId = ?1 AND " +
        "item.messageType = com.okatu.rgan.feed.constant.FeedMessageType.COMMENT_VOTE")
    Optional<FeedMessageBoxItem> findCommentVoteItemByMessageId(Long messageId);

    boolean existsByReceiverAndReadIsFalse(RganUser receiver);

    List<FeedMessageBoxItem> findAllByIdInAndReceiver(Collection<Long> id, RganUser receiver);

    int countByReceiverAndMessageTypeAndMessageStatusAndReadIsFalse(RganUser receiver, FeedMessageType messageType, FeedMessageStatus messageStatus);

    @Query(value = "SELECT COUNT(item) FROM FeedMessageBoxItem item " +
        "WHERE item.receiver= ?1 AND " +
        "item.messageStatus = ?2 AND " +
        "item.read = FALSE AND" +
        "(item.messageType=com.okatu.rgan.feed.constant.FeedMessageType.BLOG_VOTE OR item.messageType=com.okatu.rgan.feed.constant.FeedMessageType.COMMENT_VOTE)"
    )
    int countUnreadVoteItemByReceiverAndMessageStatus(RganUser receiver, FeedMessageStatus messageStatus);
}
