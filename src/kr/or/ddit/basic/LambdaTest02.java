package kr.or.ddit.basic;

public class LambdaTest02 {

	public static void main(String[] args) {
		/*
		 * // 람다식을 사용하지 않았을 경우 LambdaTestInterface01 t1 = new LambdaTestInterface01() {
		 * 
		 * @Override public void test() { System.out.println("안녕하세요..."); } };
		 * t1.test();
		 */

		// 람다식은 추상 메서드 하나만 선언할 수 있다. -> 2개면 오류
		LambdaTestInterface01 t2 = () -> {
			System.out.println("안녕하세요2");
		};
		t2.test();

		LambdaTestInterface01 t3 = () -> {
			System.out.println("안녕하세요3");
		};
		t3.test();

		System.out.println("---------------------------------------------------");
		// ---------------------------------------------------
		LambdaTestInterface02 t4 = (int a) -> {
			int result = a + 10;
			System.out.println(result);
		};
		t4.test(50);

		LambdaTestInterface02 t5 = b -> {
			int result = b + 20;
			System.out.println(result);
		};
		t5.test(70);

		LambdaTestInterface02 t6 = a -> System.out.println(a + 30);

		t6.test(90);

		System.out.println("---------------------------------------------------");

		LambdaTestInterface03 t7 = (int a, int b) -> {
			int r = a + b;
			return r;
		};
		int k = t7.test(3, 8);
		System.out.println("k = " + k);

		LambdaTestInterface03 t8 = (a, b) -> {
			return a * b;
		};

		k = t8.test(10, 20);
		System.out.println("k = " + k);

		LambdaTestInterface03 t9 = (a, b) -> a - b;

		k = t9.test(200, 50);
		System.out.println("k = " + k);
		
		
	}
}
