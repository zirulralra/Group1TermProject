package multi;

import java.util.List;

public class Message {
    private String id;
    private String msg;
    private String type;
    private String rcvid;
    private List<String> check;
    private int people;
    private String profile;

    public Message() {}

    // 6개짜리 생성자
    public Message(String id, String msg, String type, String rcvid, List<String> check, int people) {
        this.id = id;
        this.msg = msg;
        this.type = type;
        this.rcvid = rcvid;
        this.check = check;
        this.people = people;
    }

    // 7개짜리 생성자 대응용 (null 포함 가능)
    public Message(String id, String msg, String type, String rcvid, String dummyTarget, List<String> check, int people) {
        this(id, msg, type, rcvid, check, people);
        // dummyTarget은 쓰이지 않음
    }

    // ===== Getter & Setter =====

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRcvid() {
        return rcvid;
    }

    public void setRcvid(String rcvid) {
        this.rcvid = rcvid;
    }

    public List<String> getCheck() {
        return check;
    }

    public void setCheck(List<String> check) {
        this.check = check;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
