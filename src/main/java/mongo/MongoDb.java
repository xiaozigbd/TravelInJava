package mongo;
  
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
  
public class MongoDb {  
    private static MongoCollection<Document> collection;
    private static MongoCollection<Document> newCollection;
  
    /** 
     * 链接数据库 
     *  
     * @param databaseName 
     *            数据库名称 
     * @param collectionName 
     *            集合名称 
     * @param hostName 
     *            主机名 
     * @param port 
     *            端口号 
     */  
    public static void connect(String databaseName, String collectionName,  
            String hostName, int port) {  
        @SuppressWarnings("resource")  
        MongoClient client = new MongoClient(hostName, port);  
        MongoDatabase db = client.getDatabase(databaseName);  
        collection = db.getCollection(collectionName);  
        System.out.println(collection);  
    }

    /**
     * 链接数据库 ,喊
     *
     * @param databaseName
     *            数据库名称
     * @param collectionName
     *            集合名称
     * @param hostName
     *            主机名
     * @param port
     *            端口号
     */
    public static void connect(String databaseName, String collectionName,
                               String hostName, int port, String userName, String passWord) {
        @SuppressWarnings("resource")
        MongoClient client = getMongoClient(databaseName,  hostName,  port,  userName, passWord);
        MongoDatabase db = client.getDatabase(databaseName);
        collection = db.getCollection(collectionName);
//        System.out.println(collection);
    }

    /**
     * 链接数据库 ,喊
     *
     * @param databaseName
     *            数据库名称
     * @param collectionName
     *            集合名称
     * @param hostName
     *            主机名
     * @param port
     *            端口号
     */
    public static void connectNewContact(String databaseName, String collectionName,
                               String hostName, int port, String userName, String passWord) {
        @SuppressWarnings("resource")
        MongoClient client = getMongoClient(databaseName,  hostName,  port,  userName, passWord);
        MongoDatabase db = client.getDatabase(databaseName);
        newCollection = db.getCollection(collectionName);
//        System.out.println(collection);
    }

    public static MongoClient getMongoClient(String databaseName, String hostName, int port, String userName, String passWord){
        //连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
        //ServerAddress()两个参数分别为 服务器地址 和 端口
        ServerAddress  serverAddress = new ServerAddress(hostName, port);
        List<ServerAddress> addrs = new ArrayList<>();
        addrs.add(serverAddress);

        //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
        MongoCredential credential = MongoCredential.createScramSha1Credential(userName, databaseName, passWord.toCharArray());
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
        credentials.add(credential);

        //通过连接认证获取MongoDB连接
        MongoClient mongoClient = new MongoClient(addrs,credentials);
        return mongoClient;
    }
  
    /** 
     * 插入一个文档 
     *  
     * @param document 
     *            文档 
     */  
    public static void insert(Document document) {  
        collection.insertOne(document);  
    }  
  
    /** 
     * 查询所有文档 
     *  
     * @return 所有文档集合 
     */  
    public static List<Document> findAll() {  
        List<Document> results = new ArrayList<Document>();  
        FindIterable<Document> iterables = collection.find();
        MongoCursor<Document> cursor = iterables.iterator();
        while (cursor.hasNext()) {  
            results.add(cursor.next());  
        }  
  
        return results;  
    }  
  
    /** 
     * 根据条件查询 
     *  
     * @param filter 
     *            查询条件 //注意Bson的几个实现类，BasicDBObject, BsonDocument, 
     *            BsonDocumentWrapper, CommandResult, Document, RawBsonDocument 
     * @return 返回集合列表 
     */  
    public static List<Document> findBy(Bson filter, Integer limit) {
        List<Document> results = new ArrayList<>();
        FindIterable<Document> iterables;
        if(null!=limit){
            iterables = collection.find(filter).limit(limit);
        }else {
            iterables = collection.find(filter);
        }
        MongoCursor<Document> cursor = iterables.iterator();
        while (cursor.hasNext()) {  
            results.add(cursor.next());  
        }  
  
        return results;  
    }

    /**
     * 得到long
     * @param filter
     * @return
     */
    public static Long count(Bson filter){
        return collection.count(filter);
    }
  
    /** 
     * 更新查询到的第一个 
     *  
     * @param filter 
     *            查询条件 
     * @param update 
     *            更新文档 
     * @return 更新结果 
     */  
    public static UpdateResult updateOne(Bson filter, Bson update) {  
        UpdateResult result = collection.updateOne(filter, update);  
  
        return result;  
    }  
  
    /** 
     * 更新查询到的所有的文档 
     *  
     * @param filter 
     *            查询条件 
     * @param update 
     *            更新文档 
     * @return 更新结果 
     */  
    public static UpdateResult updateMany(Bson filter, Bson update) {  
        UpdateResult result = collection.updateMany(filter, update);  
  
        return result;  
    }  
  
    /** 
     * 更新一个文档, 结果是replacement是新文档，老文档完全被替换 
     *  
     * @param filter 
     *            查询条件 
     * @param replacement 
     *            跟新文档 
     */  
    public static void replace(Bson filter, Document replacement) {  
        collection.replaceOne(filter, replacement);  
    }  
  
    /** 
     * 根据条件删除一个文档 
     *  
     * @param filter 
     *            查询条件 
     */  
    public static void deleteOne(Bson filter) {  
        collection.deleteOne(filter);  
    }  
  
    /** 
     * 根据条件删除多个文档 
     *  
     * @param filter 
     *            查询条件 
     */  
    public static void deleteMany(Bson filter) {  
        collection.deleteMany(filter);  
    }  
}  