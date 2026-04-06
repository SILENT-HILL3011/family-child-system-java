package com.child.common.utils;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SliderCaptchaUtil {

    private final Map<String, Integer> cache = new ConcurrentHashMap<>();
    private final Random random = new Random();
    private static final int BLOCK_W = 50;
    private static final int BLOCK_H = 50;
    private static final int MARGIN = 12;

    // 柔和渐变色背景
    private final Color[][] bgList = {
            {new Color(251, 239, 242), new Color(245, 215, 222)},
            {new Color(230, 247, 238), new Color(204, 235, 216)},
            {new Color(228, 242, 254), new Color(198, 224, 250)},
            {new Color(254, 247, 229), new Color(251, 233, 194)},
            {new Color(241, 234, 247), new Color(225, 204, 237)}
    };

    public Map<String, String> createSliderCaptcha() {
        try {
            int w = 300;
            int h = 180;

            // 随机背景
            Color[] bg = bgList[random.nextInt(bgList.length)];
            BufferedImage bgImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bgImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setPaint(new GradientPaint(0, 0, bg[0], 0, h, bg[1]));
            g.fillRect(0, 0, w, h);

            // 水印
            g.setColor(new Color(110, 110, 130, 160));
            g.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            g.drawString("家庭育儿助手", 14, 32);

            // 缺口位置
            int gapX = 80 + random.nextInt(w - 160);
            int gapY = MARGIN + random.nextInt(h - BLOCK_H - MARGIN * 2);

            // 画拼图缺口
            drawPuzzle(g, gapX, gapY, true);
            g.dispose();

            // 滑块图片
            BufferedImage blockImage = new BufferedImage(BLOCK_W, BLOCK_H, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = blockImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255));
            drawPuzzle(g2, 0, 0, false);
            g2.dispose();

            String key = java.util.UUID.randomUUID().toString();
            cache.put(key, gapX);

            return Map.of(
                    "key", key,
                    "bg", imageToBase64(bgImage),
                    "block", imageToBase64(blockImage),
                    "y", String.valueOf(gapY)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 拼图形状
    private void drawPuzzle(Graphics2D g, int x, int y, boolean isHole) {
        int r = 9;
        Polygon p = new Polygon();

        p.addPoint(x, y);
        p.addPoint(x + BLOCK_W / 2 - r, y);
        p.addPoint(x + BLOCK_W / 2, y - (isHole ? r : 0));
        p.addPoint(x + BLOCK_W / 2 + r, y);
        p.addPoint(x + BLOCK_W, y);

        p.addPoint(x + BLOCK_W, y + BLOCK_H / 2 - r);
        p.addPoint(x + BLOCK_W + (isHole ? 0 : r), y + BLOCK_H / 2);
        p.addPoint(x + BLOCK_W, y + BLOCK_H / 2 + r);
        p.addPoint(x + BLOCK_W, y + BLOCK_H);

        p.addPoint(x, y + BLOCK_H);
        p.addPoint(x, y + BLOCK_H / 2 + r);
        p.addPoint(x - (isHole ? 0 : r), y + BLOCK_H / 2);
        p.addPoint(x, y + BLOCK_H / 2 - r);
        p.addPoint(x, y);

        g.fillPolygon(p);
    }

    private String imageToBase64(BufferedImage image) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bos);
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(bos.toByteArray());
    }

    // 校验
    public boolean checkSlider(String key, String moveXStr) {
        if (key == null || moveXStr == null) return false;
        try {
            int moveX = Integer.parseInt(moveXStr);
            Integer realX = cache.remove(key);
            return realX != null && Math.abs(moveX - realX) <= 5;
        } catch (Exception e) {
            return false;
        }
    }
}