package com.kelefa.cmpp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import com.kelefa.cmpp.result.Result;
import com.kelefa.cmpp.result.CancelResult;

public final class Cancel
{
  public static final int LEN = 8;

  protected byte[] msg_id = new byte[LEN];

  public Cancel()
  {
  }

  public void setMsg_id( String s )
      throws OutOfBoundsException
  {
    if ( s.length() > LEN )
      throw new OutOfBoundsException();

    byte[] tem = s.getBytes();
    System.arraycopy( tem, 0, msg_id, 0, tem.length );
  }

  public void execute( ConnDesc conn )
      throws IOException, OutOfBoundsException
  {
    Header header = new Header();

    header.pk_cmd = Const.CMPPE_CANCEL;
    header.pk_seq = conn.getSeq();
    header.pk_len += LEN;

    DataOutputStream out = new DataOutputStream( conn.sock.getOutputStream() );
    header.send( out );
    out.write( msg_id );
  }

  public static Result respond(DataInputStream in,Header header)
      throws IOException
  {
    CancelResult result = new CancelResult();
    result.success_Id = in.readByte();
    return result;
  }


}
