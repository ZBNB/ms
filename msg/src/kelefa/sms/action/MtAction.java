package com.kelefa.sms.action;

import com.kelefa.cmpp.mt.MtManager;
import com.kelefa.glidewindow.GlideWindow;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionSupport;


public class MtAction
    extends ActionSupport
{
  private String tel;
  private String content;
  private String Submit;
  private String loginMessage;
  private int lastActiveTest;
  private int todoSize;
  private int waitSize;
  private String result;

  public MtAction()
  {
  }

  public String execute()
      throws Exception
  {
    MtManager manager = MtManager.getInstance();
    if ( Submit != null ) {
      manager.submit( tel, content );
      result = "发送成功";
    }

    boolean logined = manager.isLogined();
    loginMessage = logined ? "已经链接" : "没有链接";

    lastActiveTest = ( ( int ) ( System.currentTimeMillis() -
				 manager.getLastActiveTest() ) / 1000 );

    GlideWindow glideWindow = manager.getGlideWindow();
    todoSize = glideWindow.getTodoSize();
    waitSize = glideWindow.getWaitSize();

    return Action.SUCCESS;
  }

  public void setTel( String tel )
  {
    this.tel = tel;
  }

  public void setContent( String content )
  {
    this.content = content;
  }

  public void setSubmit( String Submit )
  {
    this.Submit = Submit;
  }

  public String getTel()
  {
    return tel;
  }

  public String getLoginMessage()
  {
    return loginMessage;
  }

  public int getLastActiveTest()
  {
    return lastActiveTest;
  }

  public int getTodoSize()
  {
    return todoSize;
  }

  public int getWaitSize()
  {
    return waitSize;
  }

  public String getResult()
  {
    return result;
  }

}
