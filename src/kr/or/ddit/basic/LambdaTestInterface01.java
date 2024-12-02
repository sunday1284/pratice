package kr.or.ddit.basic;
//함수적 인터페이스 -> 하나의 추상클래스만 가능
@FunctionalInterface //사용자가 임의적으로 메서드를 추가하는것을 방지할 수 있다. -> 오류 방지
public interface LambdaTestInterface01 {
	// 반환값이 없고 매개변수도 없는 메서드
	public void test(); 
}

@FunctionalInterface
interface LambdaTestInterface02 {
	// 반환값이 없고 매개변수가 있는 메서드
	public void test(int a);
}

@FunctionalInterface
interface LambdaTestInterface03 {
	// 반환값이 있고 매개변수도 있는 메서드
	public int test(int a, int b);
}