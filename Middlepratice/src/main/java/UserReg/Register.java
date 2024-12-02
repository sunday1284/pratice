package UserReg;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.google.gson.Gson;


@WebServlet("/register")
public class Register extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;
    

	
	public void init() throws ServletException {
		try {
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
					.build(Resources.getResourceAsStream("mybatis/mybatis-config.xml"));
			userDAO = new UserDAO(sqlSessionFactory);
		} catch (IOException e) {
			throw new ServletException("MyBatis 오류입니다...",e);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		
		Map<String, Object> result = new HashMap<>();
		try {
			userDAO.registerUser(username, password, email);
			result.put("status", "success");
			result.put("massage", "회원가입 성공!");
		} catch (Exception e) {
			result.put("status", "error");
			result.put("massage", "회원가입 실패~~: " +e.getMessage());
		}
		PrintWriter out = response.getWriter();
		out.write(new Gson().toJson(result));
		out.flush();
	}

}
