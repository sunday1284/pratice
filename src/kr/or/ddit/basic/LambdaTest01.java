package kr.or.ddit.basic;

/*
 	- 람다식 ==> 메서드를 하나의 식으로 표현한 것.
  		   ==> 익명함수를 생성하기 위한 식
  		   ==> 자바에서는 '매개변수를 가진 코드 블록'으로 
  		   	   '런타임시' '익명구현체'로 생성된다.
  	****************************************  	
  	- 메서드의 기본형식 
  	반환값타입 메서드이름(매개변수들...){
  		
  		처리할 내용들;
  		...
  	}
  	
  	- 람다식으로 변환
  	(매개변수들...) -> {
  		처리할 내용들;
  		...
  	}
  	****************************************
  	형식) 인터페이스명 변수명 = 람다식;
  	
  	람다식 형식)
  	기본틀-> (자료형이름 변수명, ...) -> { 실행문들; ... }
  	1) '자료형이름'을 생략할 수 있다.
  		(int a) -> {System.out.println(a);}
  		(a) -> {System.out.println(a);}
  		
  	2) 매개변수가 1개만 있으면 괄호('( )')를 생략할 수 있다.
  		a -> {System.out.println(a); }
  		
  	3) 매개변수가 없거나 2개 이상이면 괄호( '()' )를 생략할 수 없다.
  		() -> {System.out.println("안녕하세요");}
  		(a,b) ->{System.out.println(a+b)'}
  		
  	4) 실행문이 1개면 중괄호('{ }')를 생략할 수 있다.
  		a -> System.out.println(a);
  	
  	5) 반환값이 있을 경우에는 return명령을 사용한다.
  		(a,b) -> {return a+b;}
  	
  	6) 실행문에 return문만 있는 경우에는 return명령과 중괄호('{}')를 생략할 수 있다.
 		(a,b) -> a + b;
 		
 	-  하나의 추상 메서드만 선언된 인터페이스만 람다식으로 변환할 수 있다.
 			=> 이런 인터페이스를 '함수적 인터페이스'라고 한다.
 			=> 함수적 인터페이스를 나타내는 annotation ==> @FuntionalInterface
 */

public class LambdaTest01 {

	public static void main(String[] args) {
		// Runnable 인터페이스는 '함수적 인터페이스'이다.
		
		//람다식을 사용하지 않은 경우 (익명구현체로 구현한 경우)
		Thread th1 = new Thread(new Runnable() {			
			@Override
			public void run() {
				for(int i=1; i<=10; i++) {
					System.out.println(i);
				}
			}
		});
		
		th1.start();
		
		// 람다식을 사용하는 경우
		Thread th2 = new Thread(() -> {
			for(int i=1; i<=10; i++) {
				System.out.println("람다식-" +i);	
			}
		});
		th2.start();
	}

}
