package com.kelefa.cmpp;

import java.net.Socket;

public final class ConnDesc
{

  public Socket sock;
  private int seq;
  public int status;

  public ConnDesc()
  {
  }

  public int getSeq()
  {
    seq++;
    if ( seq == 0x7fffffff )
      seq = 1;
    return seq;
  }


  public int curentSeq()
  {
    return seq;
  }

}
