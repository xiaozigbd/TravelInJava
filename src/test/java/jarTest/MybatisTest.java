package jarTest;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by yangzhi on 2018/2/8.
 *
 * @author yangzhi
 */
public class MybatisTest {
    @Test
    public void testSth() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        sqlSession.selectOne("SELECT USER_NAME FROM CASE_INFO WHERE USER_ID = 1");
    }
}
