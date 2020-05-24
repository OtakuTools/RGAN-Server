package com.okatu.rgan.blog.model.param;

import javax.validation.constraints.Size;

public class CommentEditParam {
    @Size(max = 255)
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
