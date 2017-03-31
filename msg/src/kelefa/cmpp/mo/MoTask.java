package com.kelefa.cmpp.mo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.kelefa.cmpp.ConnDesc;
import com.kelefa.cmpp.Const;
import com.kelefa.cmpp.Deliver;
import com.kelefa.cmpp.Header;
import org.apache.log4j.Logger;

public class MoTask implements Runnable
{
  private static final Logger log = Logger.getLogger(MoTask.class);

  private ConnDesc conn;
  private DeliverHandler deliverHandler;
  private MoManager moManager;

  public MoTask(MoManager moManager,ConnDesc conn,DeliverHandler deliverHandler)
  {
    this.moManager = moManager;
    this.conn = conn;
    this.deliverHandler = deliverHandler;
  }

  public void run()
  {
    try {
      for ( ;; )
      {
	DataInputStream in = new DataInputStream( conn.sock.getInputStream() );
	DataOutputStream out = new DataOutputStream( conn.sock.getOutputStream() );

	Header header = new Header();
	header.read( in );
	log.debug("Header.command = "+header.pk_cmd);

	switch ( header.pk_cmd ) {
	  case Const.CMPPE_DELIVER:
	    Deliver deliver = new Deliver();
	    deliver.execute( in, out, header );

	    deliverHandler.handle(deliver);
	    break;
	  case Const.CMPPE_ACTIVE:
	    Header respHeader = new Header();
	    respHeader.pk_cmd = Const.CMPPE_ACTIVE_RESP;
	    respHeader.pk_len += 1;
	    respHeader.pk_seq = header.pk_seq;
	    respHeader.send( out );
	    out.writeByte( 0 ); // reserved
	    moManager.lastActiveTest = System.currentTimeMillis();
	    break;
	  case Const.CMPPE_ACTIVE_RESP:
	    in.readByte(); // reserved
	    moManager.lastActiveTest = System.currentTimeMillis();
	    break;
	}
      }
    }
    catch ( IOException ex ) {
      ex.printStackTrace();
      moManager.logined = false;
      moManager.login();
    }
  }
}
