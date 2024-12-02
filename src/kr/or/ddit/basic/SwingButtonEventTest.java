package kr.or.ddit.basic;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


// 이벤트 리스너를 외부 클래스로 작성해서 설정하기
class MyListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("버튼을 클릭했습니다...");
		
//		Toolkit toolkit = Toolkit.getDefaultToolkit();
//		toolkit.beep(); //경고음을 발생시킴
		
		// 이벤트가 발생한 객체 구하기 -> 버튼 안에 있는 메시지가 바뀜(setText 안의 문구)
		JButton btn = (JButton) e.getSource();
		
		btn.setText("버튼이 눌렸습니다.");
	}
}

class MyFrame extends JFrame {
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JLabel label;
	
	private int count = 0;
	
	//생성자 
	public MyFrame() {
		setSize(600, 150); //가로 세로 창 크기
		setTitle("이벤트 예제"); //제목
		setLayout(new FlowLayout());
		
		button1 = new JButton("버튼 클릭"); //버튼을 만들어 이름 지정
		button2 = new JButton("증가"); //버튼을 만들어 이름 지정
		button3 = new JButton("Action"); //버튼을 만들어 이름 지정
		
		label = new JLabel("현재 count = " + count);
		
		// 버튼을 이벤트 리스너와 연결하기
		button1.addActionListener(new MyListener());
		
		// 익명 구현체로 이벤트 설정
		button2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				count++;
				label.setText("현재 count = " + count);
			}
		});
		//람다식으로 이벤트 설정
		button3.addActionListener(//(ActionEvent e) -> {//방법 1
			//(e) -> {									//방법 2
			e -> {										//방법 3
			if(e.getActionCommand().equals("Action")) {
				button3.setText("확 인");
				count = 0;	//확 인을 누르면 count가 초기화된다.
				label.setText("현재 count = " + count);
			} else {
				button3.setText("Action");
			}
		});
		
		
		//버튼하고 레이블을 순서대로 추가함
		add(button1);
		add(button2);
		add(label);
		add(button3);
		
		setVisible(true);
	}
	
}
public class SwingButtonEventTest {

	public static void main(String[] args) {
		new MyFrame();
	}

}
