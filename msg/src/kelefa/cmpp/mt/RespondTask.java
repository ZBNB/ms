package com.kelefa.cmpp.mt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import com.kelefa.cmpp.CMPP;
import com.kelefa.cmpp.Cancel;
import com.kelefa.cmpp.ConnDesc;
import com.kelefa.cmpp.Const;
import com.kelefa.cmpp.Deliver;
import com.kelefa.cmpp.Header;
import com.kelefa.cmpp.Login;
import com.kelefa.cmpp.Query;
import com.kelefa.cmpp.Submit;
import com.kelefa.cmpp.result.SubmitResult;

public class RespondTask implements Runnable
{
  private static final Logger log = Logger.getLogger(RespondTask.class);

  private ConnDesc conn;
  private DataInputStream in;
  private DataOutputStream out;
  private MtManager mtManager;

  public RespondTask( ConnDesc conn, MtManager mtManager )
  {
    this.conn = conn;
    this.mtManager = mtManager;

    try {
      in = new DataInputStream( conn.sock.getInputStream() );
      out = new DataOutputStream( conn.sock.getOutputStream() );
    }
    catch ( IOException ex ) {
      ex.printStackTrace();
    }
  }

  public void run()
  {
    for ( ;; )
    {
      Header header = null;
      try {
	header = readHeader();
      }
      catch ( Exception ex ) {
	log.warn(ex.getMessage());
	ex.printStackTrace();
      }
      if ( header == null )
	break;
      log.debug( "mt.RespondTask.header.cmd = " + header.pk_cmd );

      try {
	route( header );
      }
      catch ( IOException ex ) {
	log.warn(ex.getMessage());
	ex.printStackTrace();
      }
    }
    log.info( "ÍË³öRespondTask" );
    mtManager.logined = false;
    mtManager.login();
  }

  private void route( Header header )
      throws IOException
  {
    switch ( header.pk_cmd ) {
      case Const.CMPPE_LOGIN_RESP: //1111111
	log.debug( "mt.RespondTask.CMPPE_LOGIN_RESP" );
	Login.respond( conn, header );
	break;

      case Const.CMPPE_LOGOUT_RESP: //222222
	log.debug( "mt.RespondTask.CMPPE_LOGOUT_RESP" );
	break;

      case Const.CMPPE_SUBMIT_RESP: //3333333
	log.debug( "mt.RespondTask.CMPPE_SUBMIT_RESP" );
	SubmitResult result =Submit.respond( in, header );
	MtManager.getInstance().onSubmitRespond(result,header);
	break;

      case Const.CMPPE_DELIVER: // ?????????
	log.debug( "mt.RespondTask.CMPPE_DELIVER" );
	Deliver deliver = new Deliver();
	deliver.execute( in, out, header );
	break;

      case Const.CMPPE_QUERY_RESP: //4444444444444444444
	log.debug( "mt.RespondTask.CMPPE_QUERY_RESP" );
	Query.respond( in, header );
	break;

      case Const.CMPPE_CANCEL_RESP: //66666666666666666666666
	log.debug( "mt.RespondTask.CMPPE_CANCEL_RESP" );
	Cancel.respond( in, header );
	break;

      case Const.CMPPE_ACTIVE_RESP: //7777777777777777777777777
	log.debug( "mt.RespondTask.CMPPE_ACTIVE_RESP" );
	in.read();
	break;
    }
  }

  private Header readHeader()
  {
    Header header = new Header();
    try {
      for ( ; ; ) {
        header.read( in );
        if ( header.pk_cmd == Const.CMPPE_ACTIVE ) {
	  log.debug("mt.RespondTask.CMPP_ACTIVE");
          CMPP.sendActiveResp( conn, header.pk_seq );
	  mtManager.lastActiveTest = System.currentTimeMillis();
        }
        else {
          break;
        }
      }
    }
    catch ( IOException ex ) {
      ex.printStackTrace();
      try {
        conn.sock.close();
      }
      catch ( IOException ex1 ) {
	ex1.printStackTrace();
      }
      return null;
    }

    return header;
  }
}
