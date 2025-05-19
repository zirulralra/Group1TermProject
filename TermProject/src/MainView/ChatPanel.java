package MainView;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.google.gson.Gson;

import multi.Message;

public class ChatPanel extends JPanel {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    private String userId;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    private Gson gson = new Gson();

    public ChatPanel(String userId, Socket socket, PrintWriter out, BufferedReader in) {
        this.userId = userId;
        this.socket = socket;
        this.out = out;
        this.in = in;

        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        sendButton = new JButton("전송");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage()); // 엔터로도 전송
    }

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            Message msg = new Message(userId, "", text, "msg", "all", null, 0);
            out.println(gson.toJson(msg));
            inputField.setText("");
        }
    }

    public void appendMessage(String msg) {
        chatArea.append(msg + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
}
