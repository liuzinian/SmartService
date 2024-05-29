package com.liu.forever.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.liu.forever.core.oss.TencentOssProxy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;

public class QrCodeUtil {

    public static InputStream generateQRCodeImage(String additionalText, String text, int qrCodeSize, String filePath) throws WriterException, IOException {
        // 定义二维码参数
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // 创建比特矩阵表示二维码
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hints);

        // 转换比特矩阵为BufferedImage
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);


        InputStream bgImageStream = QrCodeUtil.class.getClassLoader().getResourceAsStream("public/1.png");
        if (bgImageStream == null) {
            throw new IOException("Cannot find background image file");
        }
        // 加载背景图片
        BufferedImage backgroundImage = ImageIO.read(bgImageStream); // 替换为你的背景图片路径

        // 新建一个与背景图尺寸相同的画布（图像）
        BufferedImage combined = new BufferedImage(backgroundImage.getWidth(), backgroundImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // 获取画笔并开始画图
        Graphics2D g = (Graphics2D) combined.getGraphics();

        // 画出背景图
        g.drawImage(backgroundImage, 0, 0, null);

        // 计算二维码放置位置（居中）
        int deltaHeight = backgroundImage.getHeight() - qrCodeSize;
        int deltaWidth = backgroundImage.getWidth() - qrCodeSize;

        // 将QR Code画到新建的空白画布上，并居中显示
        g.drawImage(qrImage,
                deltaWidth / 2,
                deltaHeight / 2,
                null);


        // 在这里添加附加文本
        if (additionalText != null && !additionalText.isEmpty()) {
            // 设置字体颜色、大小等属性
            g.setColor(Color.BLACK); // 文字颜色
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14)); // 字体样式和大小

            // 获取文字尺寸，以便居中
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(additionalText);
            int textHeight = fm.getHeight();

            // 计算文字位置（这里我们放在二维码下方居中）
            int textX = (backgroundImage.getWidth() - textWidth) / 2;
            int textY = backgroundImage.getHeight() - (deltaHeight / 4); // 根据需要调整y坐标

            g.drawString(additionalText, textX, textY);
        }


        g.dispose();

        // 将最终合成的图像写入filePath指定的文件路径
        File file = new File(filePath);
        ImageIO.write(combined, "PNG", file); // 假设formatName为"PNG"


        return getInputStreamFromImage(filePath);


    }


    public static InputStream getInputStreamFromImage(String formatName) throws IOException {
        File file = new File(formatName);
        return Files.newInputStream(file.toPath());
    }

    public static String useExample(String additionalText, String fileName, String s, String desktopUrl, TencentOssProxy aliyuncsOssProxy) {
        try (InputStream inputStream = generateQRCodeImage(additionalText, s, (int) (360 * 1.5), desktopUrl)) {

            return aliyuncsOssProxy.upLoad(inputStream, fileName);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}
