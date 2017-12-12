package util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yangzhi on 2017/12/12.
 * httpClient使用
 * @author yangzhi
 */
public class HttpClientUtil {

    public static void main(String[] args){
        String soo = "https://opentest.haiermoney.com:18980/sign/index/down_mod/10d3b25d1e86684ad9f673bfc7096e77";
        try {
            getFile(soo, "D:\\我的文档\\02开发日志\\201712\\04海尔再来一波\\hello.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 下载文件
     * @param url            http://www.xxx.com/img/333.jpg
     * @param destFileName   包含路径和文件名
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void getFile(String url,  String destFileName) throws IOException {
        CloseableHttpClient httpclient = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response  = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();
            FileOutputStream fileOutputStream = new FileOutputStream(new File(destFileName));
            StreamUtils.copy(in, fileOutputStream);
            httpclient.close();
        } catch (IOException e) {
            throw e;
        }finally {
            if(null != httpclient){
                try {
                    httpclient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
