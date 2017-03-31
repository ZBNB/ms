package com.kelefa.cmpp.mt;

import java.io.IOException;

import org.apache.log4j.Logger;
import com.kelefa.cmpp.CMPP;
import com.kelefa.cmpp.ConnDesc;
import com.kelefa.cmpp.Header;
import com.kelefa.cmpp.OutOfBoundsException;
import com.kelefa.cmpp.Submit;
import com.kelefa.cmpp.result.SubmitResult;
import com.kelefa.cmpp.work.WorkQueue;
import com.kelefa.glidewindow.GlideWindow;
import com.kelefa.sms.cfg.AppProperties;

public class MtManager
{
  private static final Logger log = Logger.getLogger(MtManager.class);

  private GlideWindow glideWindow;
  private WorkQueue workQueue;
  private ConnDesc conn;
  long lastActiveTest = System.currentTimeMillis()-100000;

  boolean logined = false;

  private MtManager()
  {
    workQueue = new WorkQueue( 2 );
    glideWindow = new GlideWindow();
    glideWindow.setSize( AppProperties.getIntValue( "cm_mt_window_size" ) );

    login();
  }

  public void login()
  {
    if ( logined )
      return;

    String address = AppProperties.get("cm_ip");
    int port = AppProperties.getIntValue("cm_mt_port");
    conn = new ConnDesc();
    CMPP cmpp = new CMPP();

    log.info("准备链接到移动服务器: "+address+":"+port);
    boolean connected = false;
    for ( int i = 1; ; ++i ) {
      try {
	cmpp.cmpp_connect_to_ismg( address, port, conn );
	log.info("链接成功");
	connected = true;
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

    if ( !connected )
    {
      log.error( "不能链接到移动服务器!" );
      return;
    }

    String icp_id = AppProperties.get("cm_icp_id");
    String icp_auth = AppProperties.get("cm_icp_auth");
    int type = 2;
    int version = 0x20;
    int timestamp = ( int ) ( System.currentTimeMillis() / 1000 );

    try {
      cmpp.cmpp_login( conn, icp_id, icp_auth, type, version, timestamp );
      log.info("登陆移动网关服务器");
      logined = true;
    }
    catch ( OutOfBoundsException ex ) {
      log.error("登陆失败:"+ex.getMessage());
      return;
    }
    catch ( IOException ex ) {
      log.error("登陆失败:"+ex.getMessage());
      return;
    }

    RespondTask respondTask = new RespondTask( conn, this );
    workQueue.execute( respondTask );
    log.info("启动RespondTask线程");
  }

  private static MtManager instance;
  public static MtManager getInstance()
  {
    if ( instance == null )
    {
      synchronized (MtManager.class) {
	if ( instance == null )
	  instance = new MtManager();
      }
    }

    return instance;
  }

  public long getLastActiveTest()
  {
    return lastActiveTest;
  }

  public boolean isLogined()
  {
    return logined;
  }

  public ConnDesc getConn()
  {
    while (!logined) {
      try {
        Thread.sleep(1000);
      }
      catch (InterruptedException ex) {
      }
    }

    return conn;
  }

  public GlideWindow getGlideWindow()
  {
    return glideWindow;
  }

  public void send(Submit submit)
  {
    getConn(); // 确保已经建立链接
    MtTask task = new MtTask(submit,conn);

//    workQueue.execute(task);
    int sequence = conn.getSeq();
    submit.setSequence( sequence );
    glideWindow.addJob( new Integer( sequence ), task );
    log.debug("发送短信:"+sequence);
  }

  public void onSubmitRespond(SubmitResult result,Header header)
  {
    if ( result.result == 0 )
      glideWindow.remove( new Integer( header.pk_seq ) );
    else {
      log.warn( "sequence:" + header.pk_seq + "," + result.getResultMessage() );
      MtTask task = ( MtTask ) glideWindow.remove( new Integer( header.pk_seq ) );
    }
  }

  public void submit(String tel, String msgContent)
      throws IOException
  {
    Submit submit = defaultSubmit( 1, msgContent );

    submit.setDest_terminal_Id( new String[] {tel} );

    send( submit );
  }

  public void submit(String[] tels, String msgContent)
      throws IOException
  {
    Submit submit = defaultSubmit( tels.length, msgContent );

    submit.setDest_terminal_Id( tels );

    send( submit );
  }


  private Submit defaultSubmit(int userCount, String msgContent )
  {
    Submit submit = new Submit();

    submit.setPkTotal( ( byte ) 1 );
    submit.setPkNumber( ( byte ) 1 );
    submit.setRegisteredDelivery( ( byte ) 0 );
    submit.setMsgLevel( ( byte ) 6 );
    submit.setServiceId( "08989" );
    submit.setFeeUserType( ( byte ) 2 );
    submit.setFeeTerminalId( "08989" );
    submit.setTp_pId( ( byte ) 0 );
    submit.setTp_udhi( ( byte ) 0 );
    submit.setMsg_Fmt( ( byte ) 15 );
    submit.setMsg_src( "921169" );
    submit.setFeeType( "01" );
    submit.setFeeCode( "000000" );
    submit.setSrcId( "08989" );
    submit.setDestUsr_tl( ( byte ) userCount );
    submit.setMsgContent( msgContent );

    return submit;
  }

  public static void main(String[] args)
  {
    MtManager manage = MtManager.getInstance();
    long begin = System.currentTimeMillis();
    System.out.println(">>>>>>>>>>>> begin : "+begin);
    for (int i = 0; i < 70; i++)
    {
      try {
	manage.submit( "13978819797", "test" );
      }
      catch ( IOException ex ) {
	ex.printStackTrace();
      }
    }

  }
}
