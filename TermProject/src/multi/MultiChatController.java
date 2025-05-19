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
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.google.gson.Gson;

import MainView.MainFrame;
import User.User;
import User.UserDatabase;

public class MultiChatController implements Runnable {
    private final MultiChatUI v;
    private final MultiChatData chatData;
    private MainFrame mainFrame;

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

    public MultiChatController(MultiChatData chatData, MultiChatUI v, MainFrame mainFrame) {
        logger = Logger.getLogger(this.getClass().getName());
        this.chatData = chatData;
        this.v = v;
        this.mainFrame = mainFrame;
    }

    public MultiChatController(MultiChatData chatData, MultiChatUI v) {
        this(chatData, v, null);
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
                        outMsg.println(gson.toJson(new Message(v.id, "", "secret", rcvid, "", userList, 0)));
                        outMsg.flush();
                        v.msgInput.setText("");
                    }
                } else {
                    outMsg.println(gson.toJson(new Message(v.id, v.msgInput.getText(), "message", "all", "", userList, 0)));
                    outMsg.flush();
                    v.msgInput.setText("");
                }
            } else if (obj == v.exitButton) {
                sendLogoutAndClose(userList);
            } else if (obj == v.deleteButton) {
                outMsg.flush();
                v.c_msgOut.setText("----------------------------------- 기록 삭제 ----------------------------------\n");
            }
        });

        v.addButtonWindowListenr(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                List<String> userList = Collections.list(v.nameOutModel.elements());
                sendLogoutAndClose(userList);
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

    // 안전하게 로그아웃 보내고 종료하는 함수
    private void sendLogoutAndClose(List<String> userList) {
        try {
            String logoutJson = gson.toJson(new Message(v.id, "", "logout", "all", "", userList, 0));
            outMsg.println(logoutJson);
            outMsg.flush();
            Thread.sleep(300); // 서버가 메시지 받을 시간 확보
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(); // 소켓 안전 종료
        }
    }


    public void connectServer() {
        try {
            socket = new Socket(ip, 12345);
            logger.log(INFO, "[Client]Sever 연결 성공!!");

            inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outMsg = new PrintWriter(socket.getOutputStream(), true);

            List<String> userList = Collections.list(v.nameOutModel.elements());

            m = new Message(v.id, "", "login", "all", "", userList, 0);
            outMsg.println(gson.toJson(m));

            Message profileRequest = new Message();
            profileRequest.setType("PROFILE_REQUEST");
            profileRequest.setId(v.id);
            profileRequest.setRcvid(v.id);
            outMsg.println(gson.toJson(profileRequest));

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
                System.out.println("[DEBUG] 수신된 원시 메시지: " + msg);
                m = gson.fromJson(msg, Message.class);

                Date date = new Date();
                List<String> receivedCheckList = m.getCheck();

                if ("PROFILE_RESPONSE".equals(m.getType())) {
                    String json = m.getProfile();
                    Map<String, String> profileData = gson.fromJson(json, Map.class);
                    String nickname = profileData.get("nickname");
                    String intro = profileData.get("intro");

                    String targetId = m.getRcvid();
                    boolean isMyself = targetId.equals(v.id);

                    // 내 프로필이면 DB도 갱신
                    if (isMyself) {
                        User me = UserDatabase.shared().getUserById(v.id);
                        if (me != null) {
                            me.setNickname(nickname);
                            me.setIntro(intro);
                        }
                    }

                    // 무조건 displayUserInfo 호출, isMine 기준으로
                    SwingUtilities.invokeLater(() -> {
                        mainFrame.getFriendPanel().displayUserInfo(nickname, intro, isMyself);
                    });

                    continue;
                }


                SwingUtilities.invokeLater(() -> {
                    if (mainFrame != null && mainFrame.getFriendPanel() != null) {
                        mainFrame.getFriendPanel().updateFriendList(receivedCheckList);
                    }
                });

                v.nameOutModel.clear();
                for (String name : receivedCheckList) {
                    v.nameOutModel.addElement(name);
                }
                people = m.getPeople();
                v.nameOut.setModel(v.nameOutModel);

                if ("server".equals(m.getType())) {
                    chatData.refreshData("🟢 [알림] " + m.getId() + " " + m.getMsg() + "\n");
                } else if ("s_secret".equals(m.getType())) {
                    chatData.refreshData(m.getId() + "→" + m.getRcvid() + " : " + m.getMsg() + "               " + time_sdf.format(date) + "\n");
                } else if ("message".equals(m.getType())) {
                    chatData.refreshData(m.getId() + " : " + m.getMsg() + "               " + time_sdf.format(date) + "\n");
                }

                v.c_msgOut.setCaretPosition(v.c_msgOut.getDocument().getLength());

            } catch (IOException e) {
                logger.log(WARNING, "[MultiChatUI]메시지 스트림 종료!!");
                close();
            }
        }

        logger.info("[MultiChatUI]" + thread.getName() + " 메시지 수신 스레드 종료됨!!");
    }

    private void close() {
        try {
            if (socket != null && !socket.isClosed()) socket.close();
            if (inMsg != null) inMsg.close();
            if (outMsg != null) outMsg.close();
        } catch (IOException e) {
            logger.log(WARNING, "[MultiChatController] close() 에러 발생!!");
        }
        System.exit(0);
    }
}