package multi;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import com.google.gson.Gson;

public class MultiChatController implements Runnable
{
	// 뷰 클래스 참조 객체
	private final MultiChatUI v;
	
	// 데이터 클래스 참조 객체
	private final MultiChatData chatData;
	
	 // 소켓 연결을 위한 변수 선언
	private String ip = "127.0.0.1";
	private Socket socket;
	private BufferedReader inMsg = null;
	private PrintWriter outMsg = null;
	
	// 메시지 파싱을 위한 객체 생성
	Gson gson = new Gson();
	Message m;
	
	 // 상태 플래그
	boolean status;
	
	// 로거 객
	Logger logger;
	
	 // 메시지 수신 스레드
	Thread thread;
	
	// 메시지 전송 시간, 날짜 객체 생성
	SimpleDateFormat time_sdf = new SimpleDateFormat("a hh:mm");
	Date date = new Date();
	SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy년 MMM dd일 EEE요일", Locale.KOREA);
	
	// 접속자
	DefaultListModel<String> check = new DefaultListModel<>();
	int people;
	
	/**
     * 모델과 뷰 객체를 파라미터로 하는 생성자
     * @param chatData
     * @param v
     */
	public MultiChatController(MultiChatData chatData, MultiChatUI v)
	{
		// 로거 객체 초기화
		logger = Logger.getLogger(this.getClass().getName());
		
		 // 모델과 뷰 클래스 참조
		this.chatData = chatData;
		this.v = v;
	}
	
	 /**
     * 어플리케이션 메인 실행 메서드
     */
	public void appMain()
	{
		// 데이터 객체에서 데이터 변화를 처리할 UI 객체 추가
		chatData.addObj(v.c_msgOut);
		
		v.addEnterKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_ENTER) {
					
					Object obj = e.getSource();
					if (obj == v.idInput || obj == v.loginButton)
					{
						System.out.println("로그인 버튼 클릭");
						v.id = v.idInput.getText();
						v.idOutLabel.setText("user : " +v.id);
						v.cardLayout.show(v.tab, "logout");
						connectServer();
				
					}
					else if (obj == v.logoutButton)
					{
						System.out.println("로그아웃 버튼 클릭");
						//로그아웃 메시지 전송
						outMsg.println(gson.toJson(new Message(v.id, "", "", "logout", "all", v.nameOutModel, 0)));
						//접속자리스트 지우기
						v.nameOutModel.removeAllElements();
						v.nameOut.setModel(v.nameOutModel);
						v.idInput.setText("");
						//로그인 패널로 전환
						v.cardLayout.show(v.tab, "login");
						outMsg.close();
						
						try {
							inMsg.close();
							socket.close();
						}
						catch (IOException ex) {
							ex.printStackTrace();
						}
						status = false;
					}
					else if (obj == v.msgInput || obj == v.sendButton)
					{
						System.out.println("전송 버튼 클릭");
						if(v.secretRadio.isSelected() == true) {
							String rcvid = v.nameOut.getSelectedValue();
							if(rcvid == null) {
								JOptionPane.showMessageDialog(v.getContentPane(), "사람을 선택해주세요!");
							}
							else {
								JOptionPane.showMessageDialog(v.getContentPane(), rcvid+"님에게 귓속말을 보내시겠습니까?");
								outMsg.println(gson.toJson(new Message(v.id, "", v.msgInput.getText(), "secret", rcvid, v.nameOutModel, 0)));
			                    v.msgInput.setText("");
							}
						}
						else {
							// 메시지 전송
		                    outMsg.println(gson.toJson(new Message(v.id, "", v.msgInput.getText(), "msg", "all", v.nameOutModel, 0)));
		                    // 입력창 클리어
		                    v.msgInput.setText("");
						}
					}
					else if (obj == v.exitButton)
					{
						System.out.println("종료 버튼 클릭");
						v.nameOutModel.removeAllElements();
						v.nameOut.setModel(v.nameOutModel);
						outMsg.println(gson.toJson(new Message(v.id, "", "", "logout", "all", v.nameOutModel, 0)));
						System.exit(0);
					}
					else if (obj == v.deleteButton)
					{
						System.out.println("전체기록삭제 버튼 클릭");
						outMsg.flush();
						v.c_msgOut.setText("----------------------------------- 기록 삭제 ----------------------------------\n");
					}
					
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}
		});

		
		v.addButtonActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Object obj = e.getSource();
				
				if (obj == v.loginButton)
				{
					System.out.println("로그인 버튼 클릭");
					v.id = v.idInput.getText();
					v.idOutLabel.setText("user : " +v.id);
					v.cardLayout.show(v.tab, "logout");
					connectServer();
			
				}
				else if (obj == v.logoutButton)
				{
					System.out.println("로그아웃 버튼 클릭");
					//로그아웃 메시지 전송
					outMsg.println(gson.toJson(new Message(v.id, "", "", "logout", "all", v.nameOutModel, 0)));
					//접속자리스트 지우기
					v.contactLabel.setText(" ~ 접속자 ~");
					v.nameOutModel.removeAllElements();
					v.nameOut.setModel(v.nameOutModel);
					v.idInput.setText("");
					//로그인 패널로 전환
					v.cardLayout.show(v.tab, "login");
					outMsg.close();
					
					try {
						inMsg.close();
						socket.close();
					}
					catch (IOException ex) {
						ex.printStackTrace();
					}
					status = false;
				}
				else if (obj == v.sendButton)
				{
					System.out.println("전송 버튼 클릭");
					if(v.secretRadio.isSelected() == true) {
						String rcvid = v.nameOut.getSelectedValue();
						if(rcvid == null) {
							JOptionPane.showMessageDialog(v.getContentPane(), "사람을 선택해주세요!");
						}
						else {
							JOptionPane.showMessageDialog(v.getContentPane(), rcvid+"님에게 귓속말을 보내시겠습니까?");
							outMsg.println(gson.toJson(new Message(v.id, "", v.msgInput.getText(), "secret", rcvid, v.nameOutModel, 0)));
							System.out.println("Controller "+gson.toJson(new Message(v.id, "", v.msgInput.getText(), "secret", rcvid, v.nameOutModel, 0)));
		                    v.msgInput.setText("");
						}
					}
					else {
						// 메시지 전송
	                    outMsg.println(gson.toJson(new Message(v.id, "", v.msgInput.getText(), "msg", "all", v.nameOutModel, 0)));
	                    System.out.println("Controller "+gson.toJson(new Message(v.id, "", v.msgInput.getText(), "msg", "all", v.nameOutModel, 0)));
	                    // 입력창 클리어
	                    v.msgInput.setText("");
					}
				}
				else if (obj == v.exitButton)
				{
					System.out.println("종료 버튼 클릭");
					v.nameOutModel.removeAllElements();
					v.nameOut.setModel(v.nameOutModel);
					outMsg.println(gson.toJson(new Message(v.id, "", "", "logout", "all", v.nameOutModel, 0)));
					System.exit(0);
				}
				else if (obj == v.deleteButton)
				{
					System.out.println("전체기록삭제 버튼 클릭");
					outMsg.flush();
					v.c_msgOut.setText("----------------------------------- 기록 삭제 ----------------------------------\n");
				}
			}
		});		
		
		v.addButtonWindowListenr(new WindowListener()
		{                                                                                                                                                                                                                                                                                                                                                                                                                                     			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub	
			}
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub	
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				v.nameOutModel.removeAllElements();
				v.nameOut.setModel(v.nameOutModel);
				outMsg.println(gson.toJson(new Message(v.id, "", "", "logout", "all", v.nameOutModel, 0)));
				System.exit(0);			
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	 /**
     * 서버 접속을 위한 메서드
     */
	public void connectServer(){
		try{
			// 소켓 생성
			socket = new Socket(ip , 12345);
			logger.log(INFO, "[Client]Sever 연결 성공!!");
			
			// 입출력 스트림 생성
			inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outMsg = new PrintWriter(socket.getOutputStream(),true);
			
			// 서버에 로그인 메시지 전달
			m = new Message(v.id, "", "", "login", "all", v.nameOutModel, 0);
			outMsg.println(gson.toJson(m));
			
			// 메시지 수신을 위한 스레드 생성
			thread = new Thread(this);
			thread.start();
		}
		catch(Exception e){
			logger.log(WARNING, "[MultiChatUI]connectServer() Exception 발생!!");
			e.printStackTrace();
		}
	}
	
    /**
     * 메시지 수신을 독립적으로 처리하기 위한 스레드 실행
     */
	int count = 0;
	@Override
    public void run() {
        // 수신 메시지 처리를 위한 변수
        String msg;
        
        if(count == 0) {
        	chatData.refreshData("------------------------  "+date_sdf.format(date)+"  ------------------------\n");
        	count++;
        }

        this.status=true;

        while(status) {
            try {
                // 메시지 수신 및 파싱
                msg = inMsg.readLine();
                m = gson.fromJson(msg, Message.class);
                
                Date date = new Date();
                
                // 접속자들
                check = m.getCheck();
                people = m.getPeople();

                // MultiChatData 객체를 통해 데이터 갱신
                if(m.getType().equals("server")) {
                	v.nameOut.setModel(check);
                	v.contactLabel.setText("접속자   "+people+"명");
                	// 로그인/로그아웃 메시지 출력
                	chatData.refreshData(m.getId() + "" + m.getMsg() +"\n");
                }
                else if(m.getType().equals("s_secret")) {
                	chatData.refreshData(m.getId() + "→" + m.getRcvid() + " : " + m.getMsg() +"               "+time_sdf.format(date) +"\n");
                }
                else {
                	// 그냥 메시지 출력
                	chatData.refreshData(m.getId() + " : " + m.getMsg() +"               "+time_sdf.format(date) +"\n");
                }

                // 커서를 현재 대화 메시지에 보여줌
                v.c_msgOut.setCaretPosition(v.c_msgOut.getDocument().getLength());
                
//                if (!check.isEmpty()) {
//                	v.nameOut.setText("접속인원   "+people+"명\n"+check+"\n"); // nameOut이 JTextArea 시절
//                }
            }
            catch(IOException e)
            {
            	
                logger.log(WARNING,"[MultiChatUI]메시지 스트림 종료!!");
            }
        }
        logger.info("[MultiChatUI]" + thread.getName()+ " 메시지 수신 스레드 종료됨!!");
    }
	
	public static void main(String[] args)
	{
		MultiChatController app = new MultiChatController(new MultiChatData(), new MultiChatUI());
		app.appMain();
	}

}
