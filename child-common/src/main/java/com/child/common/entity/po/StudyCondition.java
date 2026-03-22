package com.child.common.entity.po;

public class StudyCondition {

    private Integer chineseWordCount;
    private Integer englishWordCount;
    private Integer poetryCount;
    private String status;

    public Integer getChineseWordCount() {
        return chineseWordCount;
    }

    public void setChineseWordCount(Integer chineseWordCount) {
        this.chineseWordCount = chineseWordCount;
    }

    public Integer getEnglishWordCount() {
        return englishWordCount;
    }

    public void setEnglishWordCount(Integer englishWordCount) {
        this.englishWordCount = englishWordCount;
    }

    public Integer getPoetryCount() {
        return poetryCount;
    }

    public void setPoetryCount(Integer poetryCount) {
        this.poetryCount = poetryCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
