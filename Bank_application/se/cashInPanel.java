package se;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;			// 입력 데이터의 형태 실별
import java.util.regex.*;	// 입력 데이터의 형태 식별

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.JOptionPane;

class cashInPanel extends JPanel {
	
	String input = "";
	
	ImageIcon cashoutImage = new ImageIcon(".\\Images\\Cashin\\cashInimage.jpg");
	JLabel imageLabel = new JLabel();
	
	JLabel cashinLabel = new JLabel("Choose the amount of money you want to put in.");

	JButton CashingIn = new JButton("금   액   설   정");
	JPanel mixedPanel = new JPanel(); // cashOutPanel의 가장 외곽 패널
	JPanel C_InWarningPanel = new JPanel(); // 출금패널 경고 메세지 패널_가운데
	JPanel LCashoutPanel = new JPanel(); // 출금 금액 선택 패널_왼쪽
	JPanel RCashoutPanel = new JPanel(); // 출금 금액 선택 패널_오른쪽
	JPanel C_INPanel = new JPanel(); // 금액 표시 및 출금버튼 패널
	
	protected JTextField CashIn_Field;

	//DefaultListModel payModel = new DefaultListModel();

	int result = 0;
	JLabel resultLabel = new JLabel("입금 금액 : " + result + " 원");
	JButton C_INBtn = new JButton("Cash In"); // 출금 버튼
	JButton cancelBtn = new JButton("Cancel"); // 취소 버튼
	JButton backBtn = new JButton("뒤로가기"); //뒤로가기 버튼

	Border border = BorderFactory.createTitledBorder(""); // 테두리 추가

	cashInPanel() {
		setLayout(new BorderLayout());
		cashinLabel.setHorizontalAlignment(JLabel.CENTER); // 라벨 중앙정렬
		add(cashinLabel, BorderLayout.NORTH); //
		add(mixedPanel, BorderLayout.CENTER);

		C_InWarningPanel.setBorder(border);

		mixedPanel.setLayout(new BorderLayout()); // 아랫부분 패널을 3열로 조정
		mixedPanel.add(C_InWarningPanel, BorderLayout.CENTER);
		mixedPanel.add(LCashoutPanel, BorderLayout.WEST);
		mixedPanel.add(RCashoutPanel, BorderLayout.EAST);
		mixedPanel.add(C_INPanel, BorderLayout.SOUTH);
		
		C_InWarningPanel.add(imageLabel);
		imageLabel.setIcon(cashoutImage);

		// (금액표시 + 출금버튼 패널)
		C_INPanel.setLayout(new GridLayout(3, 0, 5, 5));
		resultLabel.setHorizontalAlignment(JLabel.CENTER); // 라벨 중앙정렬	
		C_INPanel.add(resultLabel); // 출금 금액 표시 레이블
		C_INPanel.add(C_INBtn); // 출금 버튼
		C_INPanel.add(backBtn); // 뒤로가기 버튼
		
		C_INBtn.addMouseListener(new CashInClicked()); // CashOut버튼 클릭 할때 반응

		LCashoutPanel.setLayout(new GridLayout(6, 0, 5, 5)); // 세로 6칸 가로는 일렬로    //여기까지 수정했음
																// 지정해주기 위해 0으로																



		RCashoutPanel.setLayout(new GridLayout(6, 0, 5, 5)); // 출금_오른쪽

		RCashoutPanel.add(CashingIn);
		
		CashingIn.addMouseListener(new Clicked());
	}

	private class Clicked extends MouseAdapter {
		public void mouseClicked(MouseEvent c) {
			System.out.println("금액 입금 금액 설정됨");		
			
			if(c.getSource() == CashingIn){
				if(SE.smartState == true){
					input = JOptionPane.showInputDialog(mixedPanel, "금액을 입력하세요", "예) 100000"); // 괄호 앞쪽에 mixedPanel을 넣음으로써 패널의 가운데에 대화상자 생성
					System.out.println("\"" + input + "\"" + "의 금액을 입력하였습니다.");
					result = Integer.parseInt(input);
					resultLabel.setText("입금 금액 : " + result + " 원");
					
					/*   if (Pattern.matches("[0-9]", input)){
						    System.out.printf("숫자");
							resultLabel.setText("출금 금액 : " + result + " 원");    
					   }
						   else if (Pattern.matches("[A-Z]",input))
						    System.out.printf("대문자");
						   else if (Pattern.matches("[a-z]", input))
						    System.out.printf("소문자");
						   else if (Pattern.matches("[가-힣]", input))
						    System.out.println("한글");
						   else
						    System.out.printf("기타");*/
				}
				
			}
		}
	}
	
	private class CashInClicked extends MouseAdapter{ //CashOut 버튼
		public void mouseClicked(MouseEvent e) {
			if(e.getSource() == C_INBtn){
				
				if(result == 0){ //결재할 금액 없음, #참고 3
					JOptionPane.showMessageDialog(mixedPanel, "금액을 설정하십시오!", "입금 오류", JOptionPane.WARNING_MESSAGE);
				}
				else{					
					if(SE.smartInterworkState){ //연동 중이면 스마트폰으로 계산
						if(SE.banking(SmartPhonePanel.userAccountNumber.getText(), "ATM", result, '+', true)){ //거래 진행 , 입금을 할시엔 '-'를 +로 바꾸면 된다.
							System.out.println("거래 완료!" + result + "입금 되었습니다."); 
						} // 미완료시 false
					}
					else{ //연동 안함 -> 연동하기 or 취소
						Object[] options = {"연동하기", "취소"};
						int select = JOptionPane.showOptionDialog(mixedPanel, 
								"연동하시겠습니까?", "결제 오류", 
								JOptionPane.YES_NO_CANCEL_OPTION, 
								JOptionPane.QUESTION_MESSAGE, 
								null, options, options[1]);
						
						if(select == 1){ } //취소
						else{ //연동하기
							if(SE.smartState == false){ //스마트폰 안켜져있음
								JOptionPane.showMessageDialog(mixedPanel, "스마트폰을 켜주세요", "스마트폰 오류", JOptionPane.WARNING_MESSAGE);
							}
							else if(SE.smartLoginState == false){ //로그인 안되있음
								JOptionPane.showMessageDialog(mixedPanel, "로그인 해주세요", "로그인 오류", JOptionPane.WARNING_MESSAGE);
							}
							else{ //연동하기
								SE.smartInterworkState(true); //연동하기
								
								if(SE.banking(SmartPhonePanel.userAccountNumber.getText(), "ATM", result, '+', true)){ //거래 진행
								}
							}
						}
					}
				}
			}
		}
	}
}