package kr.or.ddit.basic;

public class LambdaTest03 {
	
	//메서드의 매개변수도 메서드 안에서는 지역변수이다.
	public void method(final int temp) {
		final int localVar = 40;
		int kor = 100;
		
		//람다식 내부에서 사용되는 지역변수는 final이여야 한다.
		// Java 1.8이상에서는 기본적으로 final을 붙이지 않으면 컴파일러가 
		// 자동으로 붙여준다.
		
		// temp = 100;
		/* localVar = 33; */
		kor = 300;
		
		// 람다식
		LambdaTestInterface01 It = () ->
		{
			//람다식에서 지역변수(로컬변수) 사용하기
			System.out.println("temp = " + temp);
			System.out.println("localVar = " + localVar);
		};
		It.test();
	}
	
	public static void main(String[] args) {
		LambdaTest03 lambda = new LambdaTest03();
		lambda.method(50);
			
	}

}
