package chenyunlong.zhangli;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class ImageChecker {

    public static void main(String[] args) throws FileNotFoundException {
        File file = ResourceUtils.getFile("E:\\GitHub\\cdn\\age");
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (File listFile : listFiles) {
                System.out.println(listFile.getName());
            }
        }
    }
}
