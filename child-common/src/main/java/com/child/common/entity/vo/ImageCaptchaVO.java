package com.child.common.entity.vo;

import java.util.List;

public class ImageCaptchaVO {

    private String captchaKey;         // 验证码唯一key
    private String tip;               // 提示文字
    private List<String> images;      // 9张base64图片

    public String getCaptchaKey() {
        return captchaKey;
    }

    public void setCaptchaKey(String captchaKey) {
        this.captchaKey = captchaKey;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
