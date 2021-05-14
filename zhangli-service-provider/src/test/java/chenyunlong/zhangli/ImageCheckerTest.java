package chenyunlong.zhangli;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;

public class ImageCheckerTest {

    public static void main(String[] args) throws FileNotFoundException {
        File file = ResourceUtils.getFile("E:\\GitHub\\cdn\\age");
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (File listFile : listFiles) {
                System.out.println(listFile.getName());
            }
        }
    }

    @Test
    public void getGkFromDiyGod() {

        String gks = new RestTemplate().getForObject("https://diygod.me/gk/", String.class);
        assert gks != null;
        Document document = Jsoup.parse(gks);
        Elements elements = document.getElementsByClass("gk-item");
        for (Element element : elements) {
            //像jquery选择器一样，获取文章标题元素
            element.select(".gk-img")
                    .first().getElementsByTag("picture")
                    .first().getElementsByTag("img")
                    .stream().findFirst().ifPresent(img -> {
                System.out.println(img.attr("src"));
            });
        }
    }
}
