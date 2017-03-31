package com.kelefa.cmpp.mt;

import java.io.IOException;

import org.apache.log4j.Logger;
import com.kelefa.cmpp.ConnDesc;
import com.kelefa.cmpp.Submit;
import com.kelefa.glidewindow.GlideWindow;

public class MtTask implements Runnable
{
  private static final Logger log = Logger.getLogger(MtTask.class);

  Submit submit;
  ConnDesc oldConn;

  public MtTask(Submit submit,ConnDesc conn)
  {
    this.submit = submit;
    oldConn = conn;
  }

  public void run()
  {
    try {
      ConnDesc conn = MtManager.getInstance().getConn();
      if ( oldConn != conn )
      {
	log.warn("ConnDesc对象不相等");
	GlideWindow glideWindow = MtManager.getInstance().getGlideWindow();
	glideWindow.remove( new Integer( submit.getSequence() ) );

	MtTask task = new MtTask(submit,conn);

	int sequence = conn.getSeq();
	submit.setSequence( sequence );
	glideWindow.addJob( new Integer( sequence ), task );
      }
      else
	submit.execute( conn );
    }
    catch ( IOException ex ) {
      log.warn("短信发送失败: "+ex.getMessage());
      ex.printStackTrace();
    }
  }
}
