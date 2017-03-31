package com.kelefa.cmpp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.kelefa.cmpp.result.LoginResult;

public final class Login
{

  public Login()
  {
  }

  public static void execute( ConnDesc conn, String icp_id, String icp_auth,
		       int type, int version, int timestamp )
      throws IOException, OutOfBoundsException
  {
    Header header = new Header();
    header.pk_len = Header.HEADER_LEN + 6 + 16 + 1 + 4;
    header.pk_cmd = 1;
    header.pk_seq = conn.getSeq();

    DataOutputStream out = new DataOutputStream( conn.sock.getOutputStream() );

    // 1. 发送消息头
    header.send( out );
    // 2. 发送源地址，此处为SP_Id，即SP的企业代码
    out.writeBytes(icp_id);

    SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
    String date = sdf.format(Calendar.getInstance().getTime());
    timestamp = Integer.parseInt(date);

    byte[] b_icpID = icp_id.getBytes( "GB2312" );
    byte[] b_icpPWD = icp_auth.getBytes( "GB2312" );

    byte beforeMD5[] = new byte[ b_icpPWD.length+25 ];
    System.arraycopy( b_icpID, 0, beforeMD5, 0, 6 );
    System.arraycopy( b_icpPWD, 0, beforeMD5, 15, b_icpPWD.length );
    System.arraycopy( date.getBytes(), 0, beforeMD5, 15 + b_icpPWD.length, date.length() );

    // 3. 发送AuthenticatorSource
    MD5 md5 = new MD5();
    byte[] md5ed = md5.getMD5ofStr( beforeMD5, beforeMD5.length );
    out.write(md5ed);

    if ( type > 2 || type < 0 )
      throw new OutOfBoundsException();

    // 4. 发送版本号
    out.writeByte( ( byte )version );
    // 5. 发送时间戳
    out.writeInt( timestamp );
  }

  public static LoginResult respond(ConnDesc conn)
      throws IOException
  {
    DataInputStream in = new DataInputStream( conn.sock.getInputStream() );
    Header header = new Header();
    for ( ; ; )
    {
      header.read( in );
      if ( header.pk_cmd != Const.CMPPE_ACTIVE )
	break;
      CMPP.sendActiveResp( conn, header.pk_seq );
    }

    return respond(conn,header);
  }

  public static LoginResult respond(ConnDesc conn,Header header)
      throws IOException
  {
    DataInputStream in = new DataInputStream( conn.sock.getInputStream() );

    LoginResult loginResult = new LoginResult();
    if ( header.pk_len == Header.HEADER_LEN+18 )
    {
      loginResult.stat = in.readByte();
      for ( int j = 0; j < 16; j++ )
	loginResult.auth[ j ] = in.readByte();

      loginResult.version = in.readByte();
    }
    else if (header.pk_len > Header.HEADER_LEN)
    {
	byte[] tem = new byte[ header.pk_len - Header.HEADER_LEN ];
	in.read( tem );
    }
    return loginResult;
  }

}
