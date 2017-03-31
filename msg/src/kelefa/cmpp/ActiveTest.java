package com.kelefa.cmpp;

import java.io.DataOutputStream;
import java.io.IOException;

public class ActiveTest
{
  public ActiveTest()
  {
  }

  public static void execute( ConnDesc conn )
      throws IOException
  {
    Header header = new Header();

    DataOutputStream out = new DataOutputStream( conn.sock.getOutputStream() );
    header.pk_cmd = Const.CMPPE_ACTIVE;
    header.pk_seq = conn.getSeq();
    header.send( out );
  }
}
