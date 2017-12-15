package util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangzhi on 2017/12/13.
 *
 * @author yangzhi
 */
public class ThreadPoolTest {
    public static void main(String[] a){
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-downloadContracts-%d").build();
        ExecutorService pool = new ThreadPoolExecutor(5, 200, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        for(int i=0; i< 3; i++){
            DownloadOneContract downloadOneContract = new DownloadOneContract("",
                    "https://opentest.haiermoney.com:18980/sign/index/down_mod/10d3b25d1e86684ad9f673bfc7096e77",
                    "D:\\localFile\\contracts\\"+i+"-hello.pdf");
            pool.execute(downloadOneContract);
        }
        pool.shutdown();
    }

    public static class DownloadOneContract implements Runnable {
        /**
         * 贷款编号
         */
        private String loanId;

        /**
         * 源地址
         */
        private String sourceUrl;

        /**
         * 存放地址
         */
        private String destFileName;

        public DownloadOneContract(String loanId, String sourceUrl, String destFileName) {
            this.loanId = loanId;
            this.sourceUrl = sourceUrl;
            this.destFileName = destFileName;
        }

        @Override
        public void run() {
            try {
                HttpClientUtil.getFile(sourceUrl, destFileName);
            } catch (IOException e) {
                return;
            }
        }
    }
}
