import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class ImageScraper {
    public static void main(String[] args) {
        //碧蓝航线B站wiki官方图片下载
        String url = "https://wiki.biligame.com/blhx/%E5%BD%B1%E7%94%BB%E7%9B%B8%E5%85%B3";

        try {
            // 发送HTTP请求并获取HTML页面
            Document doc = Jsoup.connect(url).get();

            // 选择所有的<img>标签
            Elements imgElements = doc.select("img");

            // 保存指定格式的图片链接地址到文本并去除重复结果
            saveUniqueImageLinksAsText(imgElements);

            System.out.println("图片链接保存完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveUniqueImageLinksAsText(Elements imgElements) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("image_links.txt"))) {
            Set<String> uniqueLinks = new HashSet<>();

            // 遍历每个<img>标签，获取图片链接地址并保存到文本
            for (Element element : imgElements) {
                // 获取图片的URL
                String imgUrl = element.absUrl("src");

                // 检查链接地址是否符合指定格式
                if (imgUrl.startsWith("https://patchwiki.biligame.com/images/blhx/thumb/") &&
                        (imgUrl.endsWith(".png") || imgUrl.endsWith(".jpg")|| imgUrl.endsWith(".jpeg") || imgUrl.endsWith(".gif"))) {
                    imgUrl = imgUrl.replace("/thumb/", "/").replaceAll("/\\d+px-.*\\.(png|jpg|jpeg|gif)", "");
                    uniqueLinks.add(imgUrl);
                }
            }

            // 将去重后的链接地址写入文本
            for (String link : uniqueLinks) {
                writer.write(link);
                writer.newLine();
            }
        }
    }
}
