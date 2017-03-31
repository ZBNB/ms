package com.kelefa.sms.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import com.kelefa.cmpp.Submit;
import com.kelefa.cmpp.mt.MtManager;
import com.kelefa.sms.dao.SmsMtDAO;
import com.kelefa.sms.guohai.SmsMt;
import com.kelefa.sms.util.HibernateUtil;

public class SmsSender
    implements Job
{
  public SmsSender()
  {
  }

  public void execute( JobExecutionContext context )
  {
    try
    {
      SmsMtDAO dao = new SmsMtDAO();
      for ( ; ; )
      {
	int count = dao.selectAllRecCount();
	if ( count == 0 )
	  break;

	MtManager manager = MtManager.getInstance();

	List result = dao.selectAllRec(1,10);
	for ( int i = 0; i < result.size(); i++ )
	{
	  SmsMt mt = ( SmsMt ) result.get( i );

	  Submit submit = new Submit();

	  submit.setPkTotal( ( byte ) 1 );
	  submit.setPkNumber( ( byte ) 1 );
	  submit.setRegisteredDelivery( ( byte ) 0 );
	  submit.setMsgLevel( ( byte ) mt.getMsg_level().intValue() );
	  submit.setServiceId( "08989" );
	  submit.setFeeUserType( ( byte ) 2 );
	  submit.setFeeTerminalId( "" );
	  submit.setTp_pId( ( byte ) 0 );
	  submit.setTp_udhi( ( byte ) 0 );
	  submit.setMsg_Fmt( ( byte ) 15 );
	  submit.setMsg_src( "921169" );
	  submit.setFeeType( mt.getFee_type() );
	  submit.setFeeCode( mt.getFee_code() );
	  submit.setSrcId( "089898" );
	  submit.setDestUsr_tl( ( byte ) 1 );
	  submit.setDest_terminal_Id( new String[] {mt.getDest_terminal_id()} );
	  submit.setMsgContent( mt.getMsg_content() );

	  manager.send(submit);


	  dao.deleteRec( mt.getId().longValue() );
	}

      }
      HibernateUtil.closeSession2();
    }
    catch ( Exception ex ) {
      ex.printStackTrace();
    }
  }
}
