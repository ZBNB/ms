package com.kelefa.cmpp.mo;

import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.Logger;
import com.kelefa.cmpp.CMPP;
import com.kelefa.cmpp.ConnDesc;
import com.kelefa.cmpp.work.WorkQueue;
import com.kelefa.sms.cfg.AppProperties;
import com.kelefa.sms.util.ComponentUtil;

public class MoManager implements DeliverHandlerAware,Serializable
{
  private static final Logger log = Logger.getLogger(MoManager.class);

  private transient WorkQueue workQueue;
  private transient DeliverHandler deliverHandler;

  boolean logined = false;
  long lastActiveTest = System.currentTimeMillis()-100000;

  private MoManager()
  {
    workQueue = new WorkQueue( 1 );
  }

  public void login()
  {
    if ( logined )
      return;

    String address = AppProperties.get("cm_ip");
    int port = AppProperties.getIntValue("cm_mo_port");
    ConnDesc conn = new ConnDesc();
    CMPP cmpp = new CMPP();
    log.info("准备链接到移动服务器: "+address+":"+port);
    for ( int i=1; ; ++i ){
      try {
        cmpp.cmpp_connect_to_ismg( address, port, conn );
	log.info("链接成功");
        logined = true;
        break;
      }
      catch ( IOException ex ) {
	log.warn( "第 " + i + " 次链接失败: " + ex.getMessage() );
	try {
	  Thread.sleep( 3000 );
	}
	catch ( InterruptedException ex1 ) {
	}

      }
    }

    if ( logined )
    {
      MoTask moTask = new MoTask( this, conn, deliverHandler );
      workQueue.execute( moTask );
      log.info("启动MO线程");
    }
    else
      log.error("不能链接到移动服务器!");
  }

  private static MoManager instance;
  public static MoManager getInstance()
  {
    if ( instance == null )
    {
      synchronized (MoManager.class) {
	if ( instance == null )
	{
	  instance = new MoManager();
	  ComponentUtil.initializeObject( instance ); // 初始化DeliverHandler
	  instance.login();
	}
      }
    }

    return instance;
  }

  public boolean isLogined()
  {
    return logined;
  }

  public long getLastActiveTest()
  {
    return lastActiveTest;
  }

  public void setDeliverHandler( DeliverHandler handler )
  {
    deliverHandler = handler;
  }
}
