package workScripts.zhiren;

import com.mongodb.client.result.UpdateResult;
import mongo.MongoConstant;
import mongo.MongoDb;
import org.bson.Document;
import util.time.DateUtil;

import java.util.Date;

/**
 * 更新所有标志位
 * 关键字：mongo
 */
public class RefreshAllToFlagOne {
    /**
     * 更新所有标志位为1
     * @param args
     */
    public static void main(String[] args){
        // 0、备份 预计要三十分钟。
        MongoDb.connect(MongoConstant.DATABASE_NAME, MongoConstant.OLD_COLLECTION_NAME, MongoConstant.IP,
                27017, MongoConstant.USER_NAME, MongoConstant.PASSWORD);

        System.out.println("更新所有标志位为1开始");
        Document filter = new Document();
        //注意update文档里要包含"$set"字段
        Document update = new Document();
        update.append("$set", new Document("flag", MongoConstant.OLD_DOCUMENT).append("updateTime", DateUtil.getMongoDate(new Date())));

        UpdateResult result = MongoDb.updateMany(filter, update);
        System.out.println("更新所有标志位为1结束");
    }
}
