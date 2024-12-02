package Rogin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		//회원 정보 저장 로직 (ex: 데이터베이스에 저장)
		// 여기서는 간단 콘솔 출력 예제
		System.out.println("회원 가입한 유저 : "+username);
		System.out.println("회원 가입한 유저 비번 : "+password);
		response.getWriter().write("회원 가입 성공!!!");
	}

}
