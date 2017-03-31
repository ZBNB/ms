package com.kelefa.cmpp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.kelefa.cmpp.result.DeliverResult;
import com.kelefa.cmpp.result.LoginResult;
import com.kelefa.cmpp.result.QueryResult;
import com.kelefa.cmpp.result.Result;
import com.kelefa.cmpp.result.SubmitResult;

public final class CMPP
{

  public void cmpp_active_test( ConnDesc conn )
      throws IOException
  {
    ActiveTest.execute( conn );
  }

  public void cmpp_cancel( ConnDesc conn, Cancel cancel )
      throws IOException, OutOfBoundsException
  {
    cancel.execute(conn);
  }

  public void cmpp_connect_to_ismg( String address, int port, ConnDesc conn )
      throws IOException
  {
    System.out.println("connecting "+address+":"+port);
    Socket socket = new Socket( address, port );
    socket.setSoTimeout( 120000 );

    conn.sock = socket;
  }

  public int cmpp_deliver( ConnDesc conn,DeliverResult deliverResult )
  {
    DataOutputStream out = null;
    DataInputStream in = null;
    Header header = new Header();

    int j = 0;
    try {
      out = new DataOutputStream( conn.sock.getOutputStream() );
      in = new DataInputStream( conn.sock.getInputStream() );
      header.pk_len = in.readInt();
      header.pk_cmd = in.readInt();
    }
    catch ( Exception _ex ) {
      header.pk_len = Header.HEADER_LEN;
      header.pk_cmd = Const.CMPPE_NACK_RESP;
      header.pk_seq = conn.getSeq();
      try {
	header.send( out );
      }
      catch ( Exception _ex2 ) {
	return 2009;
      }
      deliverResult = null;
      return 2009;
    }
    try {
      header.pk_seq = in.readInt();
      int stat = getDeliverStat( in, deliverResult, header.pk_len );
      if ( stat == -1 ) {
	out = null;
	return 2009;
      }

      Header cmppe_head2 = new Header();
      cmppe_head2.pk_cmd = Const.CMPPE_DELIVER_RESP;
      cmppe_head2.pk_len = Header.HEADER_LEN;
      cmppe_head2.pk_seq = conn.getSeq();
      cmppe_head2.send( out );
    }
    catch ( Exception _ex ) {
      j = 2009;
    }
    finally {
    }
    return j;
  }

  public void cmpp_disconnect_from_ismg( ConnDesc conn )
  {
    try {
      conn.sock.close();
    }
    catch ( Exception _ex ) {
      return;
    }
  }

  public void cmpp_login( ConnDesc conn, String icp_id, String icp_auth,
			  int type, int version, int timestamp )
      throws IOException, OutOfBoundsException
  {
    Login.execute( conn, icp_id, icp_auth, type, version, timestamp );
  }

  public void cmpp_logout( ConnDesc conn )
      throws IOException
  {
    Logout.execute( conn );
  }

  public void cmpp_query( ConnDesc conn, Query query )
      throws IOException
  {
    query.execute( conn );
  }

  public static void sendActiveResp( ConnDesc conn, int seq )
      throws IOException
  {
    Header header = new Header();

    DataOutputStream out = new DataOutputStream( conn.sock.getOutputStream() );
    header.pk_cmd = Const.CMPPE_ACTIVE_RESP;
    header.pk_seq = seq;
    header.pk_len += 1;
    header.send( out );

    out.write(0); // Reserved
  }

  public void cmpp_send_deliver_resp( ConnDesc conn, int seq, int stat )
      throws IOException
  {
    Header header = new Header();
    DataOutputStream out = new DataOutputStream( conn.sock.getOutputStream() );

    header.pk_cmd = Const.CMPPE_DELIVER_RESP;
    header.pk_seq = seq;

    header.send( out );
  }

  public void cmpp_submit( ConnDesc conn, Submit submit )
      throws IOException
  {
    submit.execute( conn );
  }

  protected int getDeliverStat( DataInputStream in, DeliverResult deliverResult,
				int i )
      throws IOException
  {
    int k = Header.HEADER_LEN;

    int j = IOUtil.readBytes( in, deliverResult.src_addr );
    if ( j == 1 )
      return 38;
    switch ( testDigitString( deliverResult.src_addr, 20 ) ) {
      case -1:
	return 5;

      case -2:
	return 38;
    }
    k += j;
    j = IOUtil.readBytes( in, deliverResult.dst_addr );
    if ( j != 1 )
      switch ( testDigitString( deliverResult.dst_addr, 20 ) ) {
	case -1:
	  return 5;
	case -2:
	  return 38;
      }
    k += j;
    j = IOUtil.readBytes( in, deliverResult.svc_type );
    if ( j != 1 && testDigitString( deliverResult.svc_type, 10 ) != 1 )
      return 5;
    k += j;
    deliverResult.proto_id = in.readByte();
    deliverResult.status_rpt = in.readByte();
    deliverResult.priority = in.readByte();
    if ( deliverResult.priority > 9 )
      return 39;
    deliverResult.data_coding = in.readByte();
    deliverResult.sm_len = in.readByte();
    int l = deliverResult.sm_len & 0xff;
    k += 5;
    if ( l > 160 )
      return 37;
    k += l;
    if ( ++k != i )
      return 5;
    IOUtil.readBytes( in, deliverResult.short_msg, l + 1 );
    if ( deliverResult.status_rpt == 1 )
      parseMsgContent( deliverResult );

    return 0;
  }

  public static void main( String[] args )
  {
    CMPP p = new CMPP();
    String s = "消息测试";

    byte icp_id[] = new byte[ 10 ];
    setBytes(icp_id,"921169");

    byte mo_message_id[] = new byte[ 64 ];
    setBytes(mo_message_id,"123456");

    byte svc_type[] = new byte[ 6 ];
    setBytes(svc_type,"9410");

    byte fee_type = 1;

    byte fee_user[] = new byte[ Const.CMPPE_MAX_MSISDN_LEN ];
    setBytes(fee_user,"13978819797");

    byte info_fee = 0;
    byte proto_id = 1;

    byte msg_mode = 0; // 消息模式:0-不需要状态报告, 1-需要状态报告, 2-控制类型消息
    byte priority = 0;
    byte fee_utype = 2;

    byte validate[] = new byte[ 10 ];
    validate[ 0 ] = 0;
    byte schedule[] = new byte[ 2 ];
    schedule[ 0 ] = 0;
    byte src_addr[] = new byte[ 12 ];
    setBytes(src_addr,"13978819797");

    byte du_count = 1;
    byte[][] dst_addr = new byte[ 10 ][ 15 ];
    setBytes(dst_addr[0],"13978819797");

    byte data_coding = 0;

    byte[] short_msg = new byte[ 150 ];
    setBytes(short_msg,"8888c8d8d8dd");
    int sm_len = "8888c8d8d8dd".length();

    ConnDesc con = null;
    try {
      con = new ConnDesc();
//      p.cmpp_connect_to_ismg( "127.0.0.1", 7890, con );
      p.cmpp_connect_to_ismg( "211.138.240.18", 7890, con );
      System.out.println( "链接成功" );
      p.cmpp_login( con, "921169", "921169", ( byte ) 2, 0x12,
		    (int)(System.currentTimeMillis()/1000) );
      System.out.println( "登陆成功" );
//      p.readPa( con );
//      System.out.println( "Read Login response" );

      Submit sub = new Submit();
//      sub.set_icpid( icp_id );
//      sub.set_svctype( svc_type );
//      sub.set_feetype( fee_type );
//      sub.set_infofee( info_fee );
//      sub.set_protoid( proto_id );
//      sub.set_msgmode( msg_mode );
//      sub.set_priority( priority );
//      sub.set_validate( validate );
//      sub.set_schedule( schedule );
//      sub.set_feeutype( fee_utype );
//      sub.set_feeuser( fee_user );
//      sub.set_srcaddr( src_addr );
//      sub.set_ducount( du_count );
//      sub.set_dstaddr( dst_addr );
//      sub.set_msg( data_coding, sm_len, short_msg );
//		sub.printAllField();

      for ( int test_count = 0; test_count < 1; test_count++ ) {
	p.cmpp_submit( con, sub );
	System.out.println( "Send submit success" );
	p.readPa( con );
      }

//      for ( int count = 0; count < 1; count++ ) {
//	p.readPa( con );
//      }
//
//      p.cmpp_logout( con );
//      p.readPa( con );
//
//      System.in.read();

    }
    catch ( Exception e ) {
      System.out.println( e.getMessage() );
      e.printStackTrace();
      System.out.println( "have a exception" );
      try {
	System.in.read();
      }
      catch ( Exception e1 ) {
      }
      return;
    }
    finally
    {
      try {
	p.cmpp_logout( con );
	p.readPa( con );

//	System.in.read();
      }
      catch ( IOException ex ) {
      }

    }
  }

  private static void setBytes( byte[] icp_id , String value)
  {
    for (int i = 0; i < value.length(); i++) {
      icp_id[i] = (byte)value.charAt(i);
    }
    icp_id[ value.length() ] = 0;
  }

  protected void parseMsgContent( DeliverResult deliverResult )
  {
    int i = 0;
    int k = 0;
    byte abyte0[] = new byte[ 4 ];
    for ( ; deliverResult.short_msg[ i ] != 9; i++ )
      deliverResult.msg_id[ i ] = deliverResult.short_msg[ i ];

    deliverResult.msg_id[ i ] = 0;
    i++;
    k = 0;
    for ( abyte0[ 0 ] = 9; deliverResult.short_msg[ i ] != 9; )
      if ( deliverResult.short_msg[ i ] == 48 && k == 0 ) {
	i++;
      }
      else {
	abyte0[ k ] = deliverResult.short_msg[ i ];
	i++;
	k++;
      }

    if ( abyte0[ 0 ] == 9 ) {
      abyte0[ 0 ] = 48;
      abyte0[ 1 ] = 0;
    }
    else {
      abyte0[ k ] = 0;
    }
    String s = new String( abyte0 );
    s = s.trim();
    deliverResult.status_from_rpt = Integer.parseInt( s );
    i++;
    for ( k = 0; deliverResult.short_msg[ i ] != 9; k++ ) {
      deliverResult.submit_time[ k ] = deliverResult.short_msg[
					       i ];
      i++;
    }

    deliverResult.submit_time[ k ] = 0;
    i++;
    for ( k = 0; deliverResult.short_msg[ i ] != 0; k++ ) {
      deliverResult.done_time[ k ] = deliverResult.short_msg[ i ];
      i++;
    }

    deliverResult.done_time[ k ] = 0;
    for ( int j = 0; j < 161; j++ )
      deliverResult.short_msg[ j ] = 0;

  }

  protected void readPa( ConnDesc conn )
  {
    try {
      Result result = readResPack_( conn );
      switch ( result.pack_id ) {
	case Const.CMPPE_NACK_RESP:
	  System.out.println( "get nack pack" );
	  break;

	case Const.CMPPE_LOGIN_RESP:
	  LoginResult loginResult = ( LoginResult )result;
	  System.out.println( "------------login resp----------: STAT = " +
			      ( ( Result ) ( loginResult ) ).stat );
	  break;

	case Const.CMPPE_LOGOUT_RESP:
	  System.out.println( "------------logout resp----------: STAT = " +
			      result.stat );
	  break;

	case Const.CMPPE_SUBMIT_RESP:
	  SubmitResult submitResult = ( SubmitResult )result;
	  System.out.println( "------------submit resp----------: STAT = " +
			      ( ( Result ) ( submitResult ) ).
			      stat + " result = " + submitResult.result );
	  Cancel cancel = new Cancel();
	  cancel.setMsg_id( new String( submitResult.msg_id ) );
	  break;

	case Const.CMPPE_DELIVER: // '\005'
	  System.out.println( "------------deliver---------: STAT = 0" );
	  DeliverResult deliverResult = ( DeliverResult )result;
	  cmpp_send_deliver_resp( conn, deliverResult.seq,
				  ( ( Result ) ( deliverResult ) ).
				  stat );
	  break;

	case Const.CMPPE_QUERY_RESP:
	  QueryResult queryResult = ( QueryResult )
						   result;
	  System.out.println( "query ::" );
//	  queryResult.printAllField();
	  break;

	case Const.CMPPE_CANCEL_RESP:
	  System.out.println( "---------cancel-----------: STAT = " +
			      result.stat );
	  break;

	case Const.CMPPE_ACTIVE_RESP:
	  System.out.println( "---------active resp-----------: STAT " +
			      result.stat );
	  break;
      }
    }
    catch ( Exception exception ) {
      System.out.println( exception.getMessage() );
      exception.printStackTrace();
      System.out.println( "have a exception" );
      try {
	System.in.read();
      }
      catch ( Exception _ex ) {}
    }
  }

  public Result readResPack_( ConnDesc conn )
      throws IOException, UnknownPackException
  {
    Pack pack = new Pack();
    DataInputStream in = new DataInputStream( conn.sock.getInputStream() );
    for ( ;; )
    {
      pack.pk_head.read( in );
      if ( pack.pk_head.pk_cmd == Const.CMPPE_ACTIVE )
	sendActiveResp( conn, pack.pk_head.pk_seq );
      else
	break;
    }

    Result result = new Result();
    result.pack_id = pack.pk_head.pk_cmd;

    switch ( pack.pk_head.pk_cmd )
    {
      case Const.CMPPE_RSP_SUCCESS:
	return result;

      case Const.CMPPE_NACK_RESP:
	return result;

      case Const.CMPPE_LOGIN_RESP://1111111
	return Login.respond(conn,pack.pk_head);

      case Const.CMPPE_LOGOUT_RESP://222222
	return result;

      case Const.CMPPE_SUBMIT_RESP://3333333
	return Submit.respond(in,pack.pk_head);

      case Const.CMPPE_DELIVER: // '\005'
	Deliver deliver = new Deliver();
	deliver.execute( null,null ,pack.pk_head );
	return result;

      case Const.CMPPE_QUERY_RESP://4444444444444444444
	return Query.respond(in,pack.pk_head);

      case Const.CMPPE_CANCEL_RESP://66666666666666666666666
	return Cancel.respond(in,pack.pk_head);

      case Const.CMPPE_ACTIVE_RESP://7777777777777777777777777
//	in.read();
	return result;
    }

    throw new UnknownPackException();
  }

  protected int string2byte( String s, byte abyte0[], int i, int j )
  {
    int k = 0;
    int l = 0;
    for ( ; k < s.length(); k++ ) {
      char c = s.charAt( k );
      c &= '\uFF00';
      c >>= '\b';
      byte byte0 = ( byte ) c;
      l++;
      if ( byte0 == 0 ) {
	char c1 = s.charAt( k );
	abyte0[ ( l - 1 ) + i ] = ( byte ) ( c1 & 0xff );
      }
      else {
	abyte0[ ( l - 1 ) + i ] = byte0;
	char c2 = s.charAt( k );
	abyte0[ l + i ] = ( byte ) ( c2 & 0xff );
	l++;
      }
    }

    if ( l > j )
      l = j;
    abyte0[ l + i ] = 0;
    return l;
  }


  protected int testDigitString( byte abyte0[], int len )
  {
    int j;
    for ( j = 0; abyte0[ j ] != 0 && j < len; j++ )
      if ( abyte0[ j ] > '9' || abyte0[ j ] < '0' )
	return -1;

    return j != len ? 1 : -2;
  }

  public CMPP()
  {
  }
}
