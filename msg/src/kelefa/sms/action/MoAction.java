package com.kelefa.sms.action;

import com.kelefa.cmpp.mo.MoManager;
import com.kelefa.sms.cfg.AppProperties;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionSupport;


public class MoAction
    extends ActionSupport
{
  private String loginMessage;
  private String ip;
  private int port;
  private int lastActiveTest;

  public MoAction()
  {
  }

  public String execute()
      throws Exception
  {
    ip = AppProperties.get( "cm_ip" );
    port = AppProperties.getIntValue( "cm_mo_port" );

    MoManager manager = MoManager.getInstance();

    boolean logined = manager.isLogined();
    loginMessage = logined ? "已经链接" : "没有链接";

    lastActiveTest = ( ( int ) ( System.currentTimeMillis() -
				 manager.getLastActiveTest() ) / 1000 );

    return Action.SUCCESS;
  }

  public String getLoginMessage()
  {
    return loginMessage;
  }

  public String getIp()
  {
    return ip;
  }

  public int getPort()
  {
    return port;
  }

  public int getLastActiveTest()
  {
    return lastActiveTest;
  }

}
