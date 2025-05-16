package multi;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import com.google.gson.Gson;

public class MultiChatController implements Runnable {
    private final MultiChatUI v;
    private final MultiChatData chatData;

    private String ip = "127.0.0.1";
    private Socket socket;
    private BufferedReader inMsg = null;
    private PrintWriter outMsg = null;

    Gson gson = new Gson();
    Message m;
    boolean status;
    Logger logger;
    Thread thread;

    SimpleDateFormat time_sdf = new SimpleDateFormat("a hh:mm");
    Date date = new Date();
    SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy년 MMM dd일 EEE요일", Locale.KOREA);

    DefaultListModel<String> check = new DefaultListModel<>();
    int people;

    public MultiChatController(MultiChatData chatData, MultiChatUI v) {
        logger = Logger.getLogger(this.getClass().getName());
        this.chatData = chatData;
        this.v = v;
    }

    public void appMain() {
        chatData.addObj(v.c_msgOut);

        v.addButtonActionListener(e -> {
            Object obj = e.getSource();
            List<String> userList = Collections.list(v.nameOutModel.elements());

            if (obj == v.sendButton) {
                if (v.secretRadio.isSelected()) {
                    String rcvid = v.nameOut.getSelectedValue();
                    if (rcvid == null) {
                        JOptionPane.showMessageDialog(v.getContentPane(), "사람을 선택해주세요!");
                    } else {
                        JOptionPane.showMessageDialog(v.getContentPane(), rcvid + "님에게 귓속말을 보내시겠습니까?");
                        outMsg.println(gson.toJson(new Message(v.id, "", v.msgInput.getText(), "secret", rcvid, userList, 0)));
                        v.msgInput.setText("");
                    }
                } else {
                    outMsg.println(gson.toJson(new Message(v.id, "", v.msgInput.getText(), "msg", "all", userList, 0)));
                    v.msgInput.setText("");
                }
            } else if (obj == v.exitButton) {
                outMsg.println(gson.toJson(new Message(v.id, "", "", "logout", "all", userList, 0)));
                System.exit(0);
            } else if (obj == v.deleteButton) {
                outMsg.flush();
                v.c_msgOut.setText("----------------------------------- 기록 삭제 ----------------------------------\n");
            }
        });

        v.addButtonWindowListenr(new WindowListener() {
            @Override public void windowClosing(WindowEvent e) {
                List<String> userList = Collections.list(v.nameOutModel.elements());
                outMsg.println(gson.toJson(new Message(v.id, "", "", "logout", "all", userList, 0)));
                System.exit(0);
            }
            public void windowOpened(WindowEvent e) {}
            public void windowClosed(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowDeactivated(WindowEvent e) {}
        });

        connectServer();
    }

    public void connectServer() {
        try {
            socket = new Socket(ip, 12345);
            logger.log(INFO, "[Client]Sever 연결 성공!!");

            inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outMsg = new PrintWriter(socket.getOutputStream(), true);

            List<String> userList = Collections.list(v.nameOutModel.elements());
            m = new Message(v.id, "", "", "login", "all", userList, 0);
            outMsg.println(gson.toJson(m));

            thread = new Thread(this);
            thread.start();
        } catch (Exception e) {
            logger.log(WARNING, "[MultiChatUI]connectServer() Exception 발생!!");
            e.printStackTrace();
        }
    }

    int count = 0;
    @Override
    public void run() {
        String msg;

        if (count == 0) {
            chatData.refreshData("------------------------  " + date_sdf.format(date) + "  ------------------------\n");
            count++;
        }

        this.status = true;

        while (status) {
            try {
                msg = inMsg.readLine();
                m = gson.fromJson(msg, Message.class);

                Date date = new Date();
                List<String> receivedCheckList = m.getCheck();
                v.nameOutModel.clear();
                for (String name : receivedCheckList) {
                    v.nameOutModel.addElement(name);
                }
                people = m.getPeople();

                if (m.getType().equals("server")) {
                    v.nameOut.setModel(v.nameOutModel);
                    //v.contactLabel.setText("접속자   " + people + "명");
                    chatData.refreshData(m.getId() + "" + m.getMsg() + "\n");
                } else if (m.getType().equals("s_secret")) {
                    chatData.refreshData(m.getId() + "→" + m.getRcvid() + " : " + m.getMsg() + "               " + time_sdf.format(date) + "\n");
                } else {
                    chatData.refreshData(m.getId() + " : " + m.getMsg() + "               " + time_sdf.format(date) + "\n");
                }

                v.c_msgOut.setCaretPosition(v.c_msgOut.getDocument().getLength());
            } catch (IOException e) {
                logger.log(WARNING, "[MultiChatUI]메시지 스트림 종료!!");
            }
        }

        logger.info("[MultiChatUI]" + thread.getName() + " 메시지 수신 스레드 종료됨!!");
    }
}