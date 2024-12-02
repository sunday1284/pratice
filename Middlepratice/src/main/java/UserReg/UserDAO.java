package UserReg;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;



public class UserDAO {
	private final SqlSessionFactory sqlSessionFactory;

	public UserDAO(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	public void registerUser(String username, String password, String email) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			
			Map<String, Object> params = new HashMap<>();
			params.put("username", username);
			params.put("password", password);
			params.put("email", email);
			
			session.insert("UserMapper.insertUser", params);
			
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		
	}
}
