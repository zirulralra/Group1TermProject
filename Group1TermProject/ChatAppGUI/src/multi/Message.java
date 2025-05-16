package multi;

import java.util.List;

public class Message {
	private String id;
	private String passwd;
	private String msg;
	private String type;
	private String rcvid;
	private List<String> check;
	private int people;
	
	public Message() {}
	
	public Message(String id, String passwd, String msg, String type, String rcvid, List<String> check, int people){
		this.id = id;
		this.passwd = passwd;
		this.msg = msg;
		this.type = type;
		this.rcvid = rcvid;
		this.check = check;
		this.people = people;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPasswd() {
		return passwd;
	}
	public void setPassword(String passwd) {
		this.passwd = passwd;
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
}
