package msg.test;

import msg.util.MsgContainer;

public class TestSms {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MsgContainer mc = new MsgContainer();
		Boolean bl = false;
		bl = mc.sendMsg("你好，测试短信2017年3月31日11:01:18", "15720310922");
		System.out.println(bl);
	}

}
