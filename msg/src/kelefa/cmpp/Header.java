package com.kelefa.cmpp;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;

public final class Header
{
  public static final int HEADER_LEN = 12;

  public int pk_len;
  public int pk_cmd;
  public int pk_seq;

  public Header()
  {
    pk_len = HEADER_LEN;
  }

  public void send( DataOutput out )
      throws IOException
  {
    out.writeInt( pk_len );
    out.writeInt( pk_cmd );
    out.writeInt( pk_seq );
  }

  public boolean read( DataInputStream in )
      throws IOException
  {
    pk_len = in.readInt();
    pk_cmd = in.readInt();
    pk_seq = in.readInt();

    return true;
  }
}
