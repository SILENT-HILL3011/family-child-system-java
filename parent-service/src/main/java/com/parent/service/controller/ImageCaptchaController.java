package com.parent.service.controller;

import com.child.common.entity.vo.ImageCaptchaVO;
import com.child.common.result.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController("/userCap")
@RequestMapping("/captcha") // 这里固定！前端才能访问
public class ImageCaptchaController {

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    private static final List<String> IMG_TYPES = Arrays.asList("car", "bicycle", "crosswalk", "tree", "person");
    private static final Random random = new Random();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/get-image")
    public R<ImageCaptchaVO> getImageCaptcha() throws Exception {
        String captchaKey = "captcha:image:" + UUID.randomUUID();
        String targetType = IMG_TYPES.get(random.nextInt(3));
        List<Integer> correctList = new ArrayList<>();
        List<String> imgBase64List = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            String type = random.nextBoolean() ? targetType : IMG_TYPES.get(random.nextInt(IMG_TYPES.size()));
            if (type.equals(targetType)) correctList.add(i);

            int num = random.nextInt(5) + 1;
            // ✅ 从 resource 读取，绝对不报错
            String path = "static/captcha/" + type + "/" + num + ".jpg";
            InputStream inputStream = new ClassPathResource(path).getInputStream();
            byte[] fileBytes = inputStream.readAllBytes();

            String base64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(fileBytes);
            imgBase64List.add(base64);
        }

        String correctJson = objectMapper.writeValueAsString(correctList);
        redisTemplate.opsForValue().set(captchaKey, correctJson, 5, TimeUnit.MINUTES);

        ImageCaptchaVO vo = new ImageCaptchaVO();
        vo.setCaptchaKey(captchaKey);
        vo.setTip("请点击所有包含【" + getLabel(targetType) + "】的图片");
        vo.setImages(imgBase64List);

        return R.success(vo);
    }

    @PostMapping("/check-image")
    public R<Void> checkImageCaptcha(@RequestBody Map<String, Object> params) throws Exception {
        String captchaKey = (String) params.get("captchaKey");
        List<Integer> userSelect = objectMapper.convertValue(params.get("userSelect"), List.class);

        String correctJson = redisTemplate.opsForValue().get(captchaKey);
        if (correctJson == null) return R.error("验证码已过期");

        List<Integer> correctList = objectMapper.readValue(correctJson, List.class);
        boolean isMatch = correctList.containsAll(userSelect) && userSelect.containsAll(correctList);

        if (!isMatch) return R.error("验证失败，请重试");

        redisTemplate.delete(captchaKey);
        return R.success();
    }

    private String getLabel(String type) {
        return switch (type) {
            case "car" -> "汽车";
            case "bicycle" -> "自行车";
            case "crosswalk" -> "斑马线";
            case "tree" -> "树木";
            case "person" -> "行人";
            default -> "目标";
        };
    }
}