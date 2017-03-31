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
    log.info("׼�����ӵ��ƶ�������: "+address+":"+port);
    for ( int i=1; ; ++i ){
      try {
        cmpp.cmpp_connect_to_ismg( address, port, conn );
	log.info("���ӳɹ�");
        logined = true;
        break;
      }
      catch ( IOException ex ) {
	log.warn( "�� " + i + " ������ʧ��: " + ex.getMessage() );
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
      log.info("����MO�߳�");
    }
    else
      log.error("�������ӵ��ƶ�������!");
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
	  ComponentUtil.initializeObject( instance ); // ��ʼ��DeliverHandler
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
