package com.kelefa.cmpp;

import java.io.IOException;
import java.io.DataOutputStream;

public class Logout
{
  public Logout()
  {
  }


  public static void execute( ConnDesc conn )
      throws IOException
  {
    Header header = new Header();

    DataOutputStream out = new DataOutputStream( conn.sock.getOutputStream() );
    header.pk_cmd = Const.CMPPE_LOGOUT;
    header.pk_seq = conn.getSeq();
    header.send( out );
  }

}
