package com.okatu.rgan.blog.model.param;

public class CommentEditParam {
    private String content;

    // identify the reply to COMMENT_ID
    private Long replyTo;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Long replyTo) {
        this.replyTo = replyTo;
    }
}
