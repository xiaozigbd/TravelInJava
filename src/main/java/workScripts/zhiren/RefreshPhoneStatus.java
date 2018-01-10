package workScripts.zhiren;


import com.mongodb.client.MongoCollection;
import mongo.MongoDbTwo;
import org.bson.Document;
import util.time.DateFormatUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by yangzhi on 2018/1/10.
 * 遍历mysql数据库的电话号码，从mongo中取得电话状态，然后更新到mysql中
 * @author yangzhi
 */
public class RefreshPhoneStatus {
    // 测试环境
//    // MYSQL 环境
//    private static final String url = "jdbc:mysql://192.168.40.201:3306/product0109?characterEncoding=utf8";
//    private static final String user = "root";
//    private static final String password = "root";
//
//    // mongo环境
//    private static final String IP = "192.168.40.202";
//    private static final String USER_NAME = "root";
//    private static final String PASSWORD = "ROOTMIME";
//    private static final String DATABASE_NAME = "csdb";
//    private static final String COLLECTION_NAME = "phone";
//    private static final String SOURCE = "csdb"; // 认证库


//生产环境
    // MYSQL 环境
    private static final String url = "jdbc:mysql://10.66.125.2:3306/cs_platform_bak?characterEncoding=utf8";
    private static final String user = "root";
    private static final String password = "nanMI2016db";

    // mongo环境
    public static final String IP = "10.66.120.100";
    public static final String USER_NAME = "csuser";
    public static final String PASSWORD = "nanMI2016";
    public static final String DATABASE_NAME = "csdb";
    private static final String COLLECTION_NAME = "phone";
    public static final String SOURCE = "admin"; // 认证库

    public static final String UPDATE_SQL
            = "UPDATE CASE_INFO SET LENDER_PHONE_STATUS = ? , UPDATE_TIME = NOW(), TEMP_FLAG = 1  WHERE TEMP_FLAG = 0 AND  LENDER_PHONE = ?";

    public static final String SELECT_TOTAL_NUM_SQL
            = "SELECT COUNT(1) TOTAL_NUM FROM CASE_INFO WHERE CASE_STATUS NOT IN (3, 5) AND TEMP_FLAG = 0";
    public static final String SELECT_SQL
            = "SELECT LENDER_PHONE FROM CASE_INFO WHERE CASE_STATUS NOT IN (3, 5) AND TEMP_FLAG = 0 LIMIT 0, 1000";


    /**
     * 得到连接
     * @return
     */
    public static Connection getConnection(){
        Connection con = null;
        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        try {
            //加载驱动程序
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url,user,password);
            if(!con.isClosed()){
                System.out.println("Succeeded connecting to the Database!");
            }
            con.setAutoCommit(true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    /**
     * 得到phone的collection
     * @return
     */
    public static MongoCollection<Document> getPhoneCollection(){
        return MongoDbTwo.connect(DATABASE_NAME, COLLECTION_NAME, IP,
                27017, USER_NAME, PASSWORD, SOURCE);
    }

    /**
     * 得到电话状态
     * @return
     */
    public static String getNewDocument(String phoneNo, MongoCollection<Document> collection){
        Document filter = new Document();
        filter.append("phone", phoneNo);
        List<Document> results = MongoDbTwo.findBy(filter, null, collection);

        if(results.size() > 0){
            if(results.size()>1){
                System.out.println(new StringBuilder("存在多条数据, cPhone:").append(phoneNo).toString());
            }
            return results.get(0).getString("phoneStatus");
        }else {
            return null;
        }
    }


    public static void main(String[] args) {
        refreshPhoneStatusNow();
    }

    /**
     * 刷新号码状态
     */
    public static void refreshPhoneStatusNow(){
        //声明Connection对象
        Connection con = getConnection();
        MongoCollection<Document> mongoCollection = getPhoneCollection();
        long loo = getPhonesTotalNum(con);
        long a = System.currentTimeMillis();
        long timeTotal;
        long totalNum = 0;
        while (true){
            long b = System.currentTimeMillis();
            List<String> phones = getPhones(con);
            if(phones.size() == 0){
                break;
            }
            for(String phone : phones){
                String status = getNewDocument(phone, mongoCollection);
                if(null != status){
                    System.out.println("有状态的号码：" + phone +"，状态：" + status);
                }
                update(con, phone, status);
            }

            timeTotal = (System.currentTimeMillis() - a)/60000;
            long timeOne = (System.currentTimeMillis() - b)/1000;
            totalNum += phones.size();
            System.out.println(DateFormatUtils.toTodayTime(new Date()) + "-总数量："+ loo +"，单次数据量："+ phones.size() +"，已经刷新案件数量：" + totalNum +
                    "， 该批次花费时间："+timeOne+ "秒，总共消耗时间" + timeTotal  +"分钟，"  );

        }

    }


    public static void update(Connection con, String phone, String status){
        //预处理更新（修改）数据，将王刚的sal改为5000.0
        try {
            PreparedStatement psql = con.prepareStatement(UPDATE_SQL);
            psql.setString(1,status);
            psql.setString(2,phone);
            psql.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到需要刷状态的电话号码，每次获得1000个
     * @return
     */
    public static List<String> getPhones(Connection con){
        List<String> loo = new ArrayList<>();
        //2.创建statement类对象，用来执行SQL语句！！
        Statement statement = null;
        try {
            statement = con.createStatement();
            //要执行的SQL语句
            //3.ResultSet类，用来存放获取的结果集！！
            ResultSet rs = statement.executeQuery(SELECT_SQL);

            while(rs.next()){
                //获取stuname这列数据
                String caseId = rs.getString("LENDER_PHONE");
                //输出结果
                loo.add(caseId);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loo;
    }

    /**
     * 得到需要刷状态的电话号码，每次获得1000个
     * @return
     */
    public static long getPhonesTotalNum(Connection con){
        //2.创建statement类对象，用来执行SQL语句！！
        Statement statement = null;
        Long loo = 0L;
        try {
            statement = con.createStatement();
            //要执行的SQL语句
            //3.ResultSet类，用来存放获取的结果集！！
            ResultSet rs = statement.executeQuery(SELECT_TOTAL_NUM_SQL);

            while(rs.next()){
                //获取stuname这列数据
                loo= rs.getLong("TOTAL_NUM");

                //输出结果
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loo;
    }
}
