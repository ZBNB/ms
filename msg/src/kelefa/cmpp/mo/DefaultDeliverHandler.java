package com.kelefa.cmpp.mo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
import com.kelefa.cmpp.Deliver;
import com.kelefa.sms.dao.SmsMoDAO;
import com.kelefa.sms.guohai.SmsMo;
import com.kelefa.sms.util.HibernateUtil;


public class DefaultDeliverHandler implements DeliverHandler
{
  private static final Logger log = Logger.getLogger(DefaultDeliverHandler.class);

  public DefaultDeliverHandler()
  {
  }

  public void handle( Deliver deliver )
  {
    log.debug( "处理上行短信," + deliver.getTerminal_Id() + "," +
	       deliver.getDest_Id() + "," + deliver.getMsg_Content() );

    SmsMoDAO dao = new SmsMoDAO();
    SmsMo mo = new SmsMo();
    mo.setCp_id( /*deliver.getService_Id()*/"921169" );
    mo.setDest_terminal_id( deliver.getTerminal_Id() );
    mo.setDestination_id( deliver.getDest_Id() );

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String time = sdf.format(Calendar.getInstance().getTime());
    mo.setIh_timestamp( time );

    mo.setMsg_content( deliver.getMsg_Content() );
    mo.setMsg_id( new Long( deliver.getMsg_Id() ) );
    mo.setRegistered_delivery( new Integer( ( int ) deliver.
					    getRegistered_Delivery() ) );

    try {
      dao.insertRec( mo );
      HibernateUtil.closeSession2();
    }
    catch ( Exception ex ) {
      ex.printStackTrace();
    }

  }
}
