package mongo;

import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import util.DateUtil;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MongoTestOne
{


    public static void main(String[] args){
        // 0、备份 预计要三十分钟。
        MongoDb.connect(MongoConstant.DATABASE_NAME, MongoConstant.OLD_COLLECTION_NAME, MongoConstant.IP,
                27017, MongoConstant.USER_NAME, MongoConstant.PASSWORD);
        // 1、给所有数据添加flag = 0
        initFlag(); // 更新标志位

        // 2、拿到为flag = 0的某条数据，

        Long num = 0L;
        System.out.println("mongo合并开始");

        long a = System.currentTimeMillis();

        boolean flag = true;
        while (flag){
            // 1、得到1000个
            List<Document> documentList = getOldDocuments();
            if(documentList.size() == 0){
                flag = false;
                continue;
            }

            System.out.println(new StringBuffer("开始第：").append(documentList.size()).append("份文档的合并；共合并数量：").append(num));
            num += documentList.size();

            for (Document oldDocument : documentList){
                if(!checkDocument(oldDocument)){
                    continue;
                }
                Document newDocument = getNewDocument(oldDocument.getString("idNo"), oldDocument.getString("cPhone"));
                if(newDocument == null){
                    addNewDocument(oldDocument); // 4.2 没有，新增
                }else {
                    updateNewDocument(oldDocument, newDocument); // 4.1 有，则更新
                }
                // 删除原来的文档
                deleteOne(oldDocument);
            }
        }

        long time = (System.currentTimeMillis() - a)/1000;
        System.out.println("mongo合并结束，花费时间："+ time  +"秒");
    }

    /**
     * 验证老的身份证号和手机号
     * @param document
     */
    private static boolean checkDocument(Document document){
        if(StringUtils.isEmpty(StringUtils.trim(document.getString("idNo"))) ||
                StringUtils.isEmpty(StringUtils.trim(document.getString("cPhone"))))  {
            System.out.println(new StringBuffer("数据有问题，idNo:").append(document.getString("idNo"))
                    .append("，cPhone：").append(document.getString("cPhone")));
            deleteOne(document);
            return false;
        }
        return true;
    }


    /**
     * 创建一个新的文档
     * @param oldDocument
     */
    public static void addNewDocument(Document oldDocument){
        Document newDocument = new Document();
        newDocument.append("idNo", oldDocument.getString("idNo"))
                .append("cName", oldDocument.getString("cName"))
                .append("cPhone", oldDocument.getString("cPhone"))
                .append("relation", oldDocument.getString("relation"))
                .append("dailingCount", oldDocument.getInteger("dailingCount"))
                .append("calledCount", oldDocument.getInteger("calledCount"))
                .append("harassCount", oldDocument.getInteger("harassCount"))
                .append("locale", oldDocument.getString("locale"))
                .append("createTime", DateUtil.getMongoDate(new Date()))
                .append("updateTime", DateUtil.getMongoDate(new Date()))
                .append("flag", MongoConstant.NEW_DOCUMENT);
        if(null != oldDocument.getInteger("channel")){
            Set<Integer> channelSet = new HashSet<>();
            channelSet.add(oldDocument.getInteger("channel"));
            newDocument.append("channelSet", channelSet);
        }
        MongoDb.insert(newDocument);
    }  
      

    /**
     * 得到合并前的文档
     * @return
     */
    public static List<Document> getOldDocuments(){
        Document filter = new Document();  
        filter.append("flag", 0);
        List<Document> results = MongoDb.findBy(filter, MongoConstant.NUM);
        return results;
    }

    /**
     * 得到合并后的文档
     * @return
     */
    public static Document getNewDocument(String idNo, String phoneNo){
        Document filter = new Document();
        filter.append("flag", 1);
        filter.append("idNo", idNo);
        filter.append("cPhone", phoneNo);
        List<Document> results = MongoDb.findBy(filter, MongoConstant.NUM);
        if(results.size() > 0){
            return results.get(0);
        }else {
            return null;
        }
    }

    /**
     * 得到合并前的文档
     * @return
     */
    public static Long getTotalNum(){
        Document filter = new Document();
        filter.append("flag", 0);
        return MongoDb.count(filter);
    }

    /**
     * 更新新的文档
     * @param oldDocument
     * @param newDocument
     */
    public static void updateNewDocument(Document oldDocument, Document newDocument){
        Document filter = new Document();  
        filter.append("idNo", newDocument.getString("idNo"));
        filter.append("cPhone", newDocument.getString("cPhone"));
        filter.append("flag", MongoConstant.NEW_DOCUMENT);

        //注意update文档里要包含"$set"字段
        Document update = new Document();

        Document doo = new Document();

        if(StringUtils.isEmpty(StringUtils.trim(oldDocument.getString("cName")))){
            doo.append("cName",oldDocument.getString("cName"));
        }
        if(StringUtils.isEmpty(StringUtils.trim(oldDocument.getString("relation")))){
            doo.append("relation",oldDocument.getString("relation"));
        }
        Integer dailingCount = oldDocument.getInteger("dailingCount");
        if(null != dailingCount && dailingCount > newDocument.getInteger("dailingCount")){
            doo.append("dailingCount", dailingCount);
        }
        Integer calledCount = oldDocument.getInteger("calledCount");
        if(null != calledCount && calledCount>newDocument.getInteger("calledCount")){
            doo.append("calledCount", calledCount);
        }
        Integer harassCount = oldDocument.getInteger("harassCount");
        if(null != harassCount && harassCount > newDocument.getInteger("harassCount")){
            doo.append("harassCount", harassCount);
        }
        if(StringUtils.isEmpty(StringUtils.trim(oldDocument.getString("locale")))){
            doo.append("locale",oldDocument.getString("locale"));
        }
        if(null != oldDocument.getInteger("channel")){
            List<Integer> loo = (List<Integer>) newDocument.get("channelSet");
            HashSet channelSet = new HashSet<>(loo);
            channelSet.add(oldDocument.getInteger("channel"));
            doo.append("channelSet", channelSet);
        }
        doo.append("updateTime", DateUtil.getMongoDate(new Date()));

        update.append("$set", doo);
        MongoDb.updateOne(filter, update);
    }

    /**
     * 初始化标志位
     */
    public static void initFlag(){
        System.out.println("初始化标志位开始");
        Document filter = new Document();
//        Bson filter = Filters.ne("flag", NEW_DOCUMENT);
        //注意update文档里要包含"$set"字段
        Document update = new Document();  
        update.append("$set", new Document("flag", MongoConstant.OLD_DOCUMENT).append("updateTime", DateUtil.getMongoDate(new Date())));

        UpdateResult result = MongoDb.updateMany(filter, update);
        System.out.println("初始化标志位结束");
    }
      
    public void testReplace(){
        Document filter = new Document();  
        filter.append("name", "zhang");  
          
        //注意：更新文档时，不需要使用"$set"  
        Document replacement = new Document();  
        replacement.append("value", 123);  
        MongoDb.replace(filter, replacement);  
    }

    /**
     * 删除老的
     */
    public static void deleteOne(Document document){
        Document filter = new Document();

        filter.append("_id", document.get("_id"));
        MongoDb.deleteOne(filter);
    }  
      
    public void testDeleteMany(){
        Document filter = new Document();  
        filter.append("gender", "male");  
        MongoDb.deleteMany(filter);  
    }  
}  