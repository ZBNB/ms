package com.kelefa.cmpp.mo;

import java.io.IOException;

import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.ChannelClosedException;
import org.jgroups.ChannelException;
import org.jgroups.ChannelListener;
import org.jgroups.ChannelNotConnectedException;
import org.jgroups.ExitEvent;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.View;

import com.kelefa.cmpp.Deliver;
import org.apache.log4j.Logger;
import java.util.Date;
import java.util.*;
import org.jgroups.Header;

public class JGroupDeliverHandler
    implements DeliverHandler,ChannelListener,Runnable
{
  private static final Logger log = Logger.getLogger(JGroupDeliverHandler.class);

  String props = "UDP(mcast_addr=228.8.8.8;mcast_port=45566;ip_ttl=32;" +
		 "mcast_send_buf_size=64000;mcast_recv_buf_size=64000):" +
		 //"PIGGYBACK(max_wait_time=100;max_size=32000):" +
		 "PING(timeout=2000;num_initial_members=3):" +
		 "MERGE2(min_interval=5000;max_interval=10000):" +
		 "FD_SOCK:" +
		 "VERIFY_SUSPECT(timeout=1500):" +
		 "pbcast.NAKACK(max_xmit_size=8096;gc_lag=50;retransmit_timeout=600,1200,2400,4800):" +
		 "UNICAST(timeout=600,1200,2400,4800):" +
		 "pbcast.STABLE(desired_avg_gossip=20000):" +
		 "FRAG(frag_size=8096;down_thread=false;up_thread=false):" +
// "CAUSAL:" +
		 "pbcast.GMS(join_timeout=5000;join_retry_timeout=2000;" +
		 "shun=false;print_local_addr=true)";
  Channel channel = null;

  public JGroupDeliverHandler()
  {
    try {
      channel = new JChannel( props );
      channel.setOpt( Channel.AUTO_RECONNECT, Boolean.TRUE );
      channel.setChannelListener( this );
      channel.connect( "sms_group" );
    }
    catch ( ChannelException ex ) {
      ex.printStackTrace();
    }

    startReceiveThread();
  }

  private void startReceiveThread()
  {
    new Thread( this ).start();
  }


  public void run()
  {
    Object tmp;
    Message msg = null;

    while ( true ) {
      try {
	tmp = channel.receive( 0 );
	if ( tmp == null )continue;

	if ( tmp instanceof View ) {
	  View v = ( View ) tmp;
	  log.debug( "** View=" + v );
	  continue;
	}

	if ( tmp instanceof ExitEvent ) {
	  log.debug(
	      "-- Draw.main(): received EXIT, waiting for ChannelReconnected callback" );
	  break;
	}

	if ( ! ( tmp instanceof Message ) )
	  continue;

	msg = ( Message ) tmp;

	Address addr = msg.getDest();
	log.debug("Address: "+ addr.toString() );
	String msgContent = ( String ) msg.getObject();
	log.info( "收到信息:"+msgContent );
      }
      catch ( ChannelNotConnectedException not ) {
	log.error( "Draw: " + not );
	break;
      }
      catch ( ChannelClosedException closed ) {
	break;
      }
      catch ( Exception e ) {
	log.error( e );
	continue;
      }
    }
  }

  public void handle( Deliver deliver )
  {
    log.debug("handle deliver: "+deliver.getDest_Id()+","+deliver.getMsg_Content());
    try {
      channel.send( new Message( null, null, deliver.getMsg_Content() ) );
    }
    catch ( ChannelClosedException ex ) {
      log.warn( ex.getMessage() );
    }
    catch ( ChannelNotConnectedException ex ) {
      log.warn( ex.getMessage() );
    }
  }

  public void channelConnected( Channel channel )
  {
    log.debug("channelConnected");
  }

  public void channelDisconnected( Channel channel )
  {
    log.debug("channelDisconnected");
  }

  public void channelClosed( Channel channel )
  {
    log.debug("channelClosed");
  }

  public void channelShunned()
  {
    log.debug("channelShunned");
  }

  public void channelReconnected( Address addr )
  {
    log.debug("channelReconnected");
  }

  public static void main(String[] args)
  {
    JGroupDeliverHandler handler = new JGroupDeliverHandler();
    Deliver deliver = new Deliver(){
      public String getDest_Id(){
	return "089890691";
      }
      public String getMsg_Content(){
	return "hello "+new Date().toString();
      }
    };
    handler.handle( deliver );
  }
}
