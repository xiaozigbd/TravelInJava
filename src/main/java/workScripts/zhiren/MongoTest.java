package workScripts.zhiren;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import mongo.MongoConstant;
import mongo.MongoDbTwo;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import util.time.DateFormatUtils;
import util.time.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 脚本介绍：从一个集合处理一下到另一个集合
 * 关键字：mongo
 */
public class MongoTest
{
    private  static MongoCollection<Document> oldCollection;
    private  static MongoCollection<Document> newCollection;
    public static void main(String[] args){
        // 0、备份 预计要三十分钟。
        oldCollection =
        MongoDbTwo.connect(MongoConstant.DATABASE_NAME, MongoConstant.OLD_COLLECTION_NAME, MongoConstant.IP,
                27017, MongoConstant.USER_NAME, MongoConstant.PASSWORD, MongoConstant.SOURCE);
        newCollection =
                MongoDbTwo.connect(MongoConstant.DATABASE_NAME, MongoConstant.NEW_COLLECTION_NAME, MongoConstant.IP,
                        27017, MongoConstant.USER_NAME, MongoConstant.PASSWORD, MongoConstant.SOURCE);

        Long num = 0L;
        Long realNum = 0L;
        System.out.println("mongo合并开始");

        long a = System.currentTimeMillis();
        long timeTotal;

        boolean flag = true;
        while (flag){
            long b = System.currentTimeMillis();
            // 1、得到1000个
            List<Document> documentList = getOldDocuments();
            if(documentList.size() == 0){
                flag = false;
                continue;
            }

            num += documentList.size();

            for (Document oldDocument : documentList){
                if(!checkDocument(oldDocument)){
                    continue;
                }
                ++realNum;
                Document newDocument = getNewDocument(oldDocument.getString("idNo"), oldDocument.getString("cPhone"));
                if(newDocument == null){
                    addNewDocument(oldDocument); // 4.2 没有，新增
                }else {
                    updateNewDocument(oldDocument, newDocument); // 4.1 有，则更新
                }
                updateOldDocument(oldDocument);
            }
            timeTotal = (System.currentTimeMillis() - a)/60000;
            long timeOne = (System.currentTimeMillis() - b)/1000;
            System.out.println(DateFormatUtils.toTodayTime(new Date()) + "-单批次合并数据量："+ documentList.size() +"，已经合并的数量：" + (realNum/10000) +
                    "万， 该批次花费时间："+timeOne+ "秒，总共消耗时间" + timeTotal  +"分钟，"  );
        }

        timeTotal = (System.currentTimeMillis() - a)/60000;
        System.out.println("mongo合并结束，花费时间："+ timeTotal  +"分钟");
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
            updateOldDocument(document);
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
                .append("dailingCount", getIntegerValue(oldDocument, "dailingCount"))
                .append("calledCount", getIntegerValue(oldDocument, "calledCount"))
                .append("harassCount", getIntegerValue(oldDocument, "harassCount"))
                .append("locale", oldDocument.getString("locale"))
                .append("createTime", DateUtil.getMongoDate(new Date()))
                .append("updateTime", DateUtil.getMongoDate(new Date()));
        if(null != getIntegerValueForChannel (oldDocument, "channel")){
            Set<Integer> channelSet = new HashSet<>();
            channelSet.add(getIntegerValueForChannel(oldDocument, "channel"));
            newDocument.append("channelSet", channelSet);
        }
        MongoDbTwo.insert(newDocument, newCollection);
    }

    /**
     * 得到Integer， 没有报错都返回0
     * @param document
     * @param key
     * @return
     */
    static Integer  getIntegerValue(Document document, String key){
        try {
            Double doo = document.getDouble(key);
            if(null == doo){
                return 0;
            }else {
                return doo.intValue();
            }
        }catch (Exception e){
            return 0;
        }

    }

    /**
     * 得到Integer， 没有报错都返回null
     * @param document
     * @param key
     * @return
     */
    static Integer  getIntegerValueForChannel(Document document, String key){
        try {
            Double doo = document.getDouble(key);
            if(null == doo){
                return null;
            }else {
                return doo.intValue();
            }
        }catch (Exception e){
            return null;
        }

    }


    /**
     * 得到合并前的文档
     * @return
     */
    public static List<Document> getOldDocuments(){
//        Document filter = new Document();
//        filter.append("flag", 0);
        Bson filter = Filters.ne("flag", MongoConstant.NEW_DOCUMENT);
        List<Document> results = MongoDbTwo.findBy(filter, MongoConstant.NUM, oldCollection);
        return results;
    }

    /**
     * 得到合并后的文档
     * @return
     */
    public static Document getNewDocument(String idNo, String phoneNo){
        Document filter = new Document();
        filter.append("idNo", idNo);
        filter.append("cPhone", phoneNo);
        List<Document> results = MongoDbTwo.findBy(filter, null, newCollection);

        if(results.size() > 0){
            if(results.size()>1){
                System.out.println(new StringBuilder("存在多条数据，idNo:").append(idNo).append("cPhone:").append(phoneNo).toString());
            }
            return results.get(0);
        }else {
            return null;
        }
    }


    /**
     * 更新新的文档
     * @param oldDocument
     * @param newDocument
     */
    public static void updateNewDocument(Document oldDocument, Document newDocument){
        Document filter = new Document();
        filter.append("_id", newDocument.get("_id"));

        //注意update文档里要包含"$set"字段
        Document update = new Document();

        Document doo = new Document();

        if(!StringUtils.isEmpty(StringUtils.trim(oldDocument.getString("cName")))){
            doo.append("cName",oldDocument.getString("cName"));
        }
        if(!StringUtils.isEmpty(StringUtils.trim(oldDocument.getString("relation")))){
            doo.append("relation",oldDocument.getString("relation"));
        }
        Integer dailingCount = getIntegerValue(oldDocument, "dailingCount");
        if(null != dailingCount && dailingCount > getIntegerValue(newDocument, "dailingCount")){
            doo.append("dailingCount", dailingCount);
        }
        Integer calledCount = getIntegerValue(oldDocument, "calledCount");
        if(null != calledCount && calledCount> getIntegerValue(newDocument, "calledCount")){
            doo.append("calledCount", calledCount);
        }
        Integer harassCount = getIntegerValue(oldDocument, "harassCount");
        if(null != harassCount && harassCount >  getIntegerValue(newDocument, "harassCount")){
            doo.append("harassCount", harassCount);
        }
        if(!StringUtils.isEmpty(StringUtils.trim(oldDocument.getString("locale")))){
            doo.append("locale",oldDocument.getString("locale"));
        }
        if(null != getIntegerValueForChannel(oldDocument, "channel")){
            List<Integer> loo = (List<Integer>) newDocument.get("channelSet");
            if(null == loo){
                loo = new ArrayList<>();
            }
            HashSet channelSet = new HashSet<>(loo);
            channelSet.add(getIntegerValueForChannel (oldDocument, "channel"));
            doo.append("channelSet", channelSet);
        }
        doo.append("updateTime", DateUtil.getMongoDate(new Date()));

        update.append("$set", doo);
        MongoDbTwo.updateOne(filter, update, newCollection);
    }


    /**
     * 更新新的文档
     * @param oldDocument
     */
    public static void updateOldDocument(Document oldDocument){
        Document filter = new Document();
        filter.append("_id", oldDocument.get("_id"));
        //注意update文档里要包含"$set"字段
        Document update = new Document();
        Document doo = new Document();
        doo.append("flag", MongoConstant.NEW_DOCUMENT);
        update.append("$set", doo);
        MongoDbTwo.updateOne(filter, update, oldCollection);
    }
}