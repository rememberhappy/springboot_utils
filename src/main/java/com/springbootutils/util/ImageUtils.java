package com.springbootutils.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.*;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ImageUtils {

    private transient static final String BASE64_PREFIX_JPG = "data:image/jpeg;base64,";
    private transient static final String BASE64_PREFIX_JPG_1 = "data:image/png;base64,";
    Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    public String imageBase64Compress(String imgBase64, float... qualitys) throws IOException {
        if (imgBase64.startsWith(BASE64_PREFIX_JPG)) {
            imgBase64 = imgBase64.replaceAll(BASE64_PREFIX_JPG, "");
        }
        if (imgBase64.startsWith(BASE64_PREFIX_JPG_1)) {
            imgBase64 = imgBase64.replaceAll(BASE64_PREFIX_JPG_1, "");
        }
        return getCompress(decodeStr(imgBase64), qualitys);
    }

    public String getImageBase64AboutUrlAndCompress(String sourceUrl) throws IOException {
        return getCompress(imageByUrl(sourceUrl));
    }

    private String getCompress(byte[] sourceByte, float... qualitys) throws IOException {
        if (sourceByte.length >= 1048576 * 2) {
            byte[] compressByte = compressPictureByQality(sourceByte, qualitys);
            return encodeImage(compressByte);
        }
        return encodeImage(sourceByte);
    }

    public byte[] imageByUrl(String imgUrl) {
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;
        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();
            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = is.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            // 对字节数组Base64编码
            return outStream.toByteArray();
        } catch (Exception e) {
            logger.error("image_url:[{}], 转化为base64失败.", url.getPath());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {

                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                }
            }
            if (httpUrl != null) {
                httpUrl.disconnect();
            }
        }
        return null;
    }

    /**
     * 图片转字符串
     *
     * @param image
     * @return
     */
    private String encodeImage(byte[] image) {
        BASE64Encoder encoder = new BASE64Encoder();
        return replaceEnter(encoder.encode(image));
    }

    public byte[] decodeStr(String base64Str) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(base64Str.replaceAll(BASE64_PREFIX_JPG, ""));
    }

    private String replaceEnter(String str) {
        String reg = "[\n-\r]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    // quality : 推荐0.5
    private static byte[] compressPictureByQality(byte[] source, float... qualitys) throws IOException {

        ByteArrayInputStream is = null;
        BufferedImage src = null;
        ByteArrayOutputStream out = null;
        ImageWriter imgWrier;
        ImageWriteParam imgWriteParams;

        float quality = 1f;
        if (qualitys != null && qualitys.length > 0) {
            quality = qualitys[0];
        }

        // 指定写图片的方式为 jpg
        imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
        imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(null);
        // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
        imgWriteParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        // 这里指定压缩的程度，参数qality是取值0~1范围内，
        imgWriteParams.setCompressionQuality(quality);
        imgWriteParams.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
        // ColorModel colorModel =ImageIO.read(new File(srcFilePath)).getColorModel();//
        ColorModel colorModel = ColorModel.getRGBdefault();
        // 指定压缩时使用的色彩模式
        imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(
                colorModel, colorModel.createCompatibleSampleModel(16, 16)));
        imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));

        try {
            is = new ByteArrayInputStream(source);
            src = ImageIO.read(is);
            int width = src.getWidth();
            int height = src.getHeight();
            out = new ByteArrayOutputStream();
            imgWrier.reset();
            // 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何
            // OutputStream构造
            imgWrier.setOutput(ImageIO.createImageOutputStream(out));
            // 调用write方法，就可以向输入流写图片
            if (width >= 4000 || height >= 4000) {
                width = width / 10;
                height = height / 10;
            } else if (width >= 2000 || height >= 2000) {
                width = (int) (width * 0.5);
                height = (int) (height * 0.5);
            }
            int w = width;
            int h = height;
            BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            image.getGraphics().fillRect(0, 0, w, h);
            image.getGraphics().drawImage(src, 0, 0, w, h, null);
            imgWrier.write(null, new IIOImage(image, null, null),
                    imgWriteParams);
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                is.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 解析本地图片或者http网络图片，并把图片加载到缓冲区
     *
     * @param path 图片路径（本地路径或者网络图片http访问路径）
     */
    public BufferedImage imageIoRead(String path) {
        try {
            BufferedImage bufferedImage;
            if (path.contains("http")) {
                //网络图片
                bufferedImage = ImageIO.read(new URL(path));
            } else {
                //本地图片
                bufferedImage = ImageIO.read(new File(path));
            }
            return bufferedImage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 给一张图片贴 图片，并生成新图片
     *
     * @param bigPath     底图路径
     * @param smallPath   要贴的图片路径
     * @param outPath     合成输出图片路径
     * @param x           贴图的位置
     * @param y           贴图的位置
     * @param smallWidth  要贴的图片宽度
     * @param smallHeight 要贴的图片高度
     */
    public void mergeImage(String bigPath, String smallPath, String outPath, String x, String y, int smallWidth, int smallHeight) {
        try {
            //加载图片
            BufferedImage small = imageIoRead(smallPath);
            BufferedImage big = imageIoRead(bigPath);
            //得到2d画笔对象
            Graphics2D g = big.createGraphics();
            float fx = Float.parseFloat(x);
            float fy = Float.parseFloat(y);
            int x_i = (int) fx;
            int y_i = (int) fy;
            g.drawImage(small, x_i, y_i, smallWidth, smallHeight, null);
            g.dispose();
            //输出图片
            ImageIO.write(big, "png", new File(outPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向画布上写多行文字文字，自动居中
     *
     * @param filePath   原图路径
     * @param text       要添加的文字
     * @param outPath    输出图片路径
     * @param font       字体
     * @param x          坐标X
     * @param y          坐标y
     * @param color      字体颜色
     * @param fontheight 字体高度
     * @param maxWeight  每行字体最大宽度
     * @param center     是否居中
     * @param rate       字体间距
     * @return int  写了几行字
     */
    public int drawTextInImg(String filePath, String text, String outPath, Font font, int x, int y, Color color, int maxWeight, int fontheight, boolean center, double rate) {
        int row = 0;
        try {
            //图片加载到缓冲区
            BufferedImage bimage = imageIoRead(filePath);
            //得到2d画笔对象
            Graphics2D g = bimage.createGraphics();
            //设置填充颜色
            g.setPaint(color);
            //设置字体
            g.setFont(font);
            //调用写写文字方法
            row = drawString(g, font, text, x, y, maxWeight, fontheight, center, rate);
            g.dispose();
            //输出图片
            FileOutputStream out = new FileOutputStream(outPath);
            ImageIO.write(bimage, "png", out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return row;
    }

    /**
     * 写文字
     *
     * @param g        2d画笔对象
     * @param font     字体
     * @param text     要添加的文字
     * @param x        坐标X
     * @param y        坐标y
     * @param maxWidth 每行字体最大宽度
     * @param height   字体高度
     * @param center   是否居中
     * @param rate     字体间距
     * @return int 写了几行字
     */
    public int drawString(Graphics2D g, Font font, String text, int x, int y, int maxWidth, int height, boolean center, double rate) {
        int row = 1;
        JLabel label = new JLabel(text);
        label.setFont(font);
        FontMetrics metrics = label.getFontMetrics(label.getFont());
        int textH = height; //metrics.getHeight();
        int textW = metrics.stringWidth(label.getText()); //字符串的宽
        String tempText = text;
        //如果字符串长度大于最大宽度，执行循环
        while (textW > maxWidth) {
            int n = textW / maxWidth;
            int subPos = tempText.length() / n;
            String drawText = tempText.substring(0, subPos);
            int subTxtW = metrics.stringWidth(drawText);
            while (subTxtW > maxWidth) {
                subPos--;
                drawText = tempText.substring(0, subPos);
                subTxtW = metrics.stringWidth(drawText);
            }
            //g.drawString(drawText, x, y);  //不调整字体间距
            MyDrawString(drawText, x, y, rate, g);
            y += textH;
            textW = textW - subTxtW;
            tempText = tempText.substring(subPos);
            row++;
        }
        //居中
        if (center) {
            x = x + (maxWidth - textW) / 2;
        }
        //g.drawString(tempText, x, y);  //不调整字体间距
        MyDrawString(tempText, x, y, rate, g);
        return row;
    }

    /**
     * 一个字一个字写，控制字体间距
     *
     * @param str  要添加的文字
     * @param x    坐标x
     * @param y    坐标y
     * @param rate 字体间距
     * @param g    画笔
     */
    public void MyDrawString(String str, int x, int y, double rate, Graphics2D g) {
        String tempStr = "";
        int orgStringWight = g.getFontMetrics().stringWidth(str);
        int orgStringLength = str.length();
        int tempx = x;
        int tempy = y;
        while (str.length() > 0) {
            tempStr = str.substring(0, 1);
            str = str.substring(1, str.length());
            g.drawString(tempStr, tempx, tempy);
            tempx = (int) (tempx + (double) orgStringWight / (double) orgStringLength * rate);
        }
    }

    /**
     * @param qality 压缩的程度
     * @param suffix 文件格式，pdf，jpg
     **/
    public ByteArrayInputStream scaleImage(InputStream inputStream, float qality, String suffix) {
        BufferedImage bufferedImage;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageWriter imgWriter;
        ImageWriteParam imgWriteParams;
        try {
            bufferedImage = ImageIO.read(inputStream);
            imgWriter = ImageIO.getImageWritersByFormatName(suffix).next();
            imgWriteParams = new JPEGImageWriteParam(null);
            // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
            imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);
            // 这里指定压缩的程度，参数qality是取值0~1范围内，
            imgWriteParams.setCompressionQuality(qality);
            imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);
            ColorModel colorModel = bufferedImage.getColorModel();// ColorModel.getRGBdefault();
            imgWriteParams.setDestinationType(new ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(32, 32)));
            imgWriter.reset();
            // OutputStream构造
            imgWriter.setOutput(ImageIO.createImageOutputStream(out));
            imgWriter.write(null, new IIOImage(bufferedImage, null, null), imgWriteParams);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        ImageUtils img = new ImageUtils();
        s2();

        /*String s1 = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAwICQoJBwwKCQoNDAwOER0TERAQESMZGxUdKiUsKyklKCguNEI4LjE/MigoOk46P0RHSktKLTdRV1FIVkJJSkf/2wBDAQwNDREPESITEyJHMCgwR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0f/wAARCAMgAjwDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD1Dyov7if98ijyov7if98ipM0ZoAj8qL+4n/fIo8qL+4n/AHyKkzRmgCPy4v8Anmn/AHyKPLi/55p/3yKkimagezRmgCLy4v+eaf98ijy4v+eaf98ipc0Z9qAIvJj/55p/3yKPJj/55p/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAI/Li/wCeafkKPLi/55p+Qp9GaAGeXF/zyT8hR5cX/PJPyFPooAOfSjn0peaOaAE59KOfSl5o5oASilooASilooAKM0UUAGaM0UUAGaM0UUAGaM0UUAGaM0YoxQAZozRijFABmjNGKMUAGaM0YoxQAZozRijFABmjNGKMUAGaM0YoxQAZozRRQAUUUUAFFFFABRRRQAUUUUAGaM0UUAGaM0UUAGaM0UUAGaM0YoxQAZozRijFABmjNGKMUAGaM0YoxQAZozRijFABmjNGKMUAGaM0YoxQAZozRijFABmjNGKMUAGaM0YoxQAZozRijFACUUtFACUUtFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFIBKKKKACiiigQUUUUAFFFFABRSNgck4FIHRujA0AOooooAKKKKACiiigBaKKKACiiimMKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKTNB6U0mgB2aKzL7V47M5cZX16Vymr/Ei0s8xwwea/s3SgTdjvc46moZ7qOBcsyj6mvKJPibM8mfsjAem6qN940OoAlopUx0Ab71A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hsH3Fqv6Vbm8vEjAyvf8ACqJcBj9K6vwRZ7ma4ccdquMbszk7I66ygEFuqgYwKs0gHalrqWiMAooopgFFFFAwooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKQ0tFFgEpaSg9KAI35BFc9r8OIzIW6d66AnrWNr65s2qJFRZxdw2RiqpHzGrE4ANVyRuNc8jVDGFMNSNTDWbGhU4FPHSox0p69KTKHjpQOtKo4pwHNBSAU/NNxTqBjj0puM04ZIoC0IRFImRVeSDIq7tzSlBimwsZT2pPem/Y61hEvpThEPShCMV7LPYflSLYj+7W4YF9KUQqKGBgvYD+7TBY45AreaEUxoBQFjHEBAqWKMg1eaIDtTQgHahjGqMCnKOaUYpTgUhgTimMc04800rSEMIpjCpSOKYw9aAIjwKjzzT2HNRtTsS2KCOani7VVB55qwmMc1SIbJlt/tV1HCnJYjP0r07RLNbKxSNRg45rlPCGk75RdSr9M13SLtUV0QiZSd2PooorUgKKKKBhRRRTAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigBKKKKAEIH92szWkBs3+XtWpWbrRxaGplsCOBuhgmqWOTWheDEhqjj731rlktTdDCKaRUhpKQxoHFKDigimGkGpOrU8GqymplNSUmSg8U0daBSr1pFXHg08UzNPBFMLhSgUU5SKBXDaaUAjvSnpTM4NCQXHnNLio94pd4xQ0FxxApjCjcDRxTSHcjK0xlxUxAqN8elDQXICOaKcaSpC41qQUrUmaQXEJqOSnsRUTHIqkS2RmoTy1SMaYPvVSRLGDmQVraRYtf3axgcA81l4O9cdzivSPCWlx29ksxAMjjP0zWsY6kS2NmytEtrdY1GMVbFHoaXvW6VjEWiiimMKKKKEAUUUUwCiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooASiiigBKzNb/48nrTrO1wZsX+lKWwI4K6P7w/SqY6H61auvvGqo6n61yy3N0IRTCcGpTyKjYVIxu+kJzTWHNJnFIWo/pTg1RZpN3FIaZZV6eHFUw9OElIdy15lKsnNVDIaFkOadh3NAv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";
        String s2 = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAwICQoJBwwKCQoNDAwOER0TERAQESMZGxUdKiUsKyklKCguNEI4LjE/MigoOk46P0RHSktKLTdRV1FIVkJJSkf/2wBDAQwNDREPESITEyJHMCgwR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0f/wAARCAMgAjwDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD1Dyov7if98ijyov7if98ipM0ZoAj8qL+4n/fIo8qL+4n/AHyKkzRmgCPy4v8Anmn/AHyKPLi/55p/3yKkimagezRmgCLy4v+eaf98ijy4v+eaf98ipc0Z9qAIvJj/55p/3yKPJj/55p/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAIvKj/uJ/3yKPKj/uJ/3yKlooAi8qP+4n/fIo8qP+4n/fIqWigCLyo/7if98ijyo/7if98ipaKAI/Li/wCeafkKPLi/55p+Qp9GaAGeXF/zyT8hR5cX/PJPyFPooAOfSjn0peaOaAE59KOfSl5o5oASilooASilooAKM0UUAGaM0UUAGaM0UUAGaM0UUAGaM0YoxQAZozRijFABmjNGKMUAGaM0YoxQAZozRijFABmjNGKMUAGaM0YoxQAZozRRQAUUUUAFFFFABRRRQAUUUUAGaM0UUAGaM0UUAGaM0UUAGaM0YoxQAZozRijFABmjNGKMUAGaM0YoxQAZozRijFABmjNGKMUAGaM0YoxQAZozRijFABmjNGKMUAGaM0YoxQAZozRijFACUUtFACUUtFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFIBKKKKACiiigQUUUUAFFFFABRSNgck4FIHRujA0AOooooAKKKKACiiigBaKKKACiiimMKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKTNB6U0mgB2aKzL7V47M5cZX16Vymr/Ei0s8xwwea/s3SgTdjvc46moZ7qOBcsyj6mvKJPibM8mfsjAem6qN940OoAlopUx0Ab71A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hsH3Fqv6Vbm8vEjAyvf8ACqJcBj9K6vwRZ7ma4ccdquMbszk7I66ygEFuqgYwKs0gHalrqWiMAooopgFFFFAwooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKQ0tFFgEpaSg9KAI35BFc9r8OIzIW6d66AnrWNr65s2qJFRZxdw2RiqpHzGrE4ANVyRuNc8jVDGFMNSNTDWbGhU4FPHSox0p69KTKHjpQOtKo4pwHNBSAU/NNxTqBjj0puM04ZIoC0IRFImRVeSDIq7tzSlBimwsZT2pPem/Y61hEvpThEPShCMV7LPYflSLYj+7W4YF9KUQqKGBgvYD+7TBY45AreaEUxoBQFjHEBAqWKMg1eaIDtTQgHahjGqMCnKOaUYpTgUhgTimMc04800rSEMIpjCpSOKYw9aAIjwKjzzT2HNRtTsS2KCOani7VVB55qwmMc1SIbJlt/tV1HCnJYjP0r07RLNbKxSNRg45rlPCGk75RdSr9M13SLtUV0QiZSd2PooorUgKKKKBhRRRTAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigBKKKKAEIH92szWkBs3+XtWpWbrRxaGplsCOBuhgmqWOTWheDEhqjj731rlktTdDCKaRUhpKQxoHFKDigimGkGpOrU8GqymplNSUmSg8U0daBSr1pFXHg08UzNPBFMLhSgUU5SKBXDaaUAjvSnpTM4NCQXHnNLio94pd4xQ0FxxApjCjcDRxTSHcjK0xlxUxAqN8elDQXICOaKcaSpC41qQUrUmaQXEJqOSnsRUTHIqkS2RmoTy1SMaYPvVSRLGDmQVraRYtf3axgcA81l4O9cdzivSPCWlx29ksxAMjjP0zWsY6kS2NmytEtrdY1GMVbFHoaXvW6VjEWiiimMKKKKEAUUUUwCiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooASiiigBKzNb/48nrTrO1wZsX+lKWwI4K6P7w/SqY6H61auvvGqo6n61yy3N0IRTCcGpTyKjYVIxu+kJzTWHNJnFIWo/pTg1RZpN3FIaZZV6eHFUw9OElIdy15lKsnNVDIaFkOadh3NAv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";
        InputStream is = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\拍照.jpg"));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] arr = new byte[1024*8];
        int len = 0;
        while ((len = is.read(arr)) != -1) {
            os.write(arr, 0, len);
        }
        ImageUtils img = new ImageUtils();
        String baseStr = img.encodeImage(os.toByteArray());
        baseStr = img.imageBase64Compress(baseStr);
        System.out.println(baseStr);
        // System.out.println(img.getCompress(imageSource));
        FileOutputStream fileOutputStream = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\拍照11.jpg"));
        fileOutputStream.write(img.decodeStr(baseStr));*/
        // s2();
        /*InputStream is = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\拍照.jpg"));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] arr = new byte[1024*8];
        int len = 0;
        while ((len = is.read(arr)) != -1) {
            os.write(arr, 0, len);
        }
        System.out.println(os.size());
        byte[] sourceByte = os.toByteArray();
        System.out.println("1: " + sourceByte.length);
        if (sourceByte.length >= 1048576*2) {
            byte[] compressByte = compressPictureByQality(sourceByte, 0.5f);
            System.out.println("2: " + compressByte.length);
            FileOutputStream fileOutputStream = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\11.jpg"));
            fileOutputStream.write(compressByte);
        }*/
    }

    public static void s2() throws Exception {
        /*InputStream is = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\1.jpg"));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] arr = new byte[1024*8];
        int len = 0;
        while ((len = is.read(arr)) != -1) {
            os.write(arr, 0, len);
        }
        ImageUtils img = new ImageUtils();
        String baseStr = img.encodeImage(os.toByteArray());
        baseStr = img.imageBase64Compress(baseStr);
        System.out.println(baseStr);
        // System.out.println(img.getCompress(imageSource));
        FileOutputStream fileOutputStream = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\33.jpg"));
        fileOutputStream.write(img.decodeStr(baseStr));
        FileOutputStream fileOutputStream1 = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\33.txt"));
        fileOutputStream1.write(baseStr.getBytes("utf-8"));

        String encrypt = AESUtil.encrypt(baseStr.getBytes("utf-8"));

        FileOutputStream fileOutputStream2 = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\encrypt.txt"));
        fileOutputStream2.write(encrypt.getBytes("utf-8"));
        //fileOutputStream.write(baseStr.getBytes("utf-8"));
        System.out.println(encrypt.getBytes("utf-8").length);

        byte[] decrypt = AESUtil.decrypt(encrypt);
        FileOutputStream fileOutputStream3 = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\decrypt.txt"));
        fileOutputStream3.write(decrypt);
        System.out.println(decrypt.length);*/
    }

    public static void s3() throws Exception {
        /*InputStream is = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\1.jpg"));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] arr = new byte[1024*8];
        int len = 0;
        while ((len = is.read(arr)) != -1) {
            os.write(arr, 0, len);
        }
        ImageUtils img = new ImageUtils();
        String baseStr = img.encodeImage(os.toByteArray());
        baseStr = img.imageBase64Compress(baseStr);
        System.out.println(baseStr);
        // System.out.println(img.getCompress(imageSource));
        FileOutputStream fileOutputStream = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\33.jpg"));
        fileOutputStream.write(img.decodeStr(baseStr));
        FileOutputStream fileOutputStream1 = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\33.txt"));
        fileOutputStream1.write(baseStr.getBytes("utf-8"));*/
    }
}
