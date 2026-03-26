package com.child.common.entity.po;

public class MessageBoardExpert {

    private String boardId;
    private Integer isFinished;
    private Integer messageCount;
    private String expertId;
    private String userId;

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public Integer getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Integer isFinished) {
        this.isFinished = isFinished;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
