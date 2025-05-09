package multi;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

public class MultiChatUI extends JFrame
{
	private JPanel contentPane;
	
	protected JLabel chatLabel;
	protected JTextArea c_msgOut;
	protected JTextField msgInput;
	protected JButton sendButton;
	
	protected JLabel userLabel;
	
	protected JLabel contactLabel;
	protected DefaultListModel<String> nameOutModel;
	protected JList<String> nameOut;
	protected JRadioButton secretRadio;
	
	protected JButton deleteButton;
	
	protected JButton exitButton;
	
	protected static String id;
	private String userId;
	
	public MultiChatUI(String userId) {
        super(userId + "님의 멀티챗방 ☆.。.:*・°☆");
        this.id = userId;
        init();
        this.setResizable(false);
    }
	
	private void init()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 510, 378);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(230, 230, 250));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane1 = new JScrollPane(c_msgOut, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane1.setBounds(12, 27, 338, 273);
		contentPane.add(scrollPane1);
		
		c_msgOut = new JTextArea();
		c_msgOut.setFont(new Font("a시네마M", Font.PLAIN, 12));
		c_msgOut.setBackground(new Color(250, 235, 215));
		c_msgOut.setEditable(false);
		c_msgOut.setLineWrap(true); // 줄바꿈 ok
		c_msgOut.setWrapStyleWord(true); // 단어 단위 줄바꿈 ok
		scrollPane1.setViewportView(c_msgOut);
		
		chatLabel = new JLabel(" ~ 채팅방 ~");
		chatLabel.setFont(new Font("a시네마M", Font.PLAIN, 13));
		chatLabel.setBounds(12, 3, 77, 25);
		contentPane.add(chatLabel);
		
		msgInput = new JTextField();
		msgInput.setFont(new Font("a시네마M", Font.PLAIN, 12));
		msgInput.setBounds(12, 313, 277, 21); // y=288
		contentPane.add(msgInput);
		msgInput.setColumns(10);
		
		sendButton = new JButton("전송");
		sendButton.setFont(new Font("a시네마M", Font.PLAIN, 12));
		sendButton.setBounds(289, 312, 61, 23); //y=287
		contentPane.add(sendButton);
		
		userLabel = new JLabel(" ~ 사용자 ~\r\n");
		userLabel.setFont(new Font("a시네마M", Font.PLAIN, 13));
		userLabel.setBounds(374, 10, 116, 15);
		contentPane.add(userLabel);
		
		contactLabel = new JLabel(" ~ 접속자 ~");
		contactLabel.setFont(new Font("a시네마M", Font.PLAIN, 13));
		contactLabel.setBounds(374, 96, 77, 15);
		contentPane.add(contactLabel);
		
		JScrollPane scrollpane2 = new JScrollPane(nameOut, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpane2.setBounds(374, 114, 116, 120);
		contentPane.add(scrollpane2);
		
		nameOutModel = new DefaultListModel<>();
		nameOut = new JList<>(nameOutModel);
		nameOut.setFont(new Font("a시네마M", Font.PLAIN, 12));
		nameOut.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scrollpane2.setViewportView(nameOut);
		
		secretRadio = new JRadioButton("귓속말");
		secretRadio.setFont(new Font("a시네마M", Font.PLAIN, 12));
		secretRadio.setBackground(new Color(230, 230, 250));
		secretRadio.setBounds(374, 240, 116, 23);
		contentPane.add(secretRadio);
		
		exitButton = new JButton("종료");
		exitButton.setFont(new Font("a시네마M", Font.PLAIN, 12));
		exitButton.setBounds(374, 311, 116, 23);
		contentPane.add(exitButton);
		
		deleteButton = new JButton("전체 기록 삭제");
		deleteButton.setFont(new Font("a시네마M", Font.PLAIN, 12));
		deleteButton.setBounds(374, 282, 116, 23);
		contentPane.add(deleteButton);
		
		setResizable(false);
		setVisible(true);  // true여야 화면에 보임.
	}
	
	public void addButtonActionListener(ActionListener listener)
	{
		sendButton.addActionListener(listener);
		deleteButton.addActionListener(listener);
		exitButton.addActionListener(listener);
	}
	
	public void addEnterKeyListener(KeyListener listener)
	{
		sendButton.addKeyListener(listener);
		deleteButton.addKeyListener(listener);
		exitButton.addKeyListener(listener);
	}
	
	public void addButtonWindowListenr(WindowListener listener)
	{
		this.addWindowListener(listener);
	}

}
