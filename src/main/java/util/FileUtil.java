package util;

import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yangzhi on 2017/12/8.
 *
 * @author yangzhi
 */
public class FileUtil {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = new FileInputStream(new File("E:\\下载\\测试\\号码状态更新模板.xlsx"));
        copyFile(inputStream, "E:\\下载\\toDir\\hello.xlsx");
    }

    /**
     * 复制文件到指定目录
     * @param is
     * @param destFileName
     * @throws IOException
     */
    public static void copyFile(InputStream is, String destFileName) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(destFileName));
        StreamUtils.copy(is, fileOutputStream);
    }

    /**
     * 常规操作
     * @param is
     * @param fos
     */
    public static void copyFile(InputStream is, FileOutputStream fos) {// 复制文件
        try {
            // 字节数组
            byte[] buffer = new byte[1024];
            // 将文件内容写到文件中
            while (is.read(buffer) != -1) {
                fos.write(buffer);
            }
            is.close();// 输入流关闭
            fos.close();// 输出流关闭
        } catch (FileNotFoundException e) {// 捕获文件不存在异常
            e.printStackTrace();
        } catch (IOException e) {// 捕获异常
            e.printStackTrace();
        }
    }
}
