package Rogin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		//로그인 검증 로직
		//간단 콘솔 출력 예제
		System.out.println("로그인한 유저 : " +username);
		System.out.println("로그인한 유저 비번: " +password);
		response.getWriter().write("로그인 성공!!!");
	}

}
