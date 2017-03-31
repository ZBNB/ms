package com.kelefa.cmpp;

import java.io.DataInputStream;
import java.io.IOException;

public final class Pack
{
    protected Header pk_head;
    protected byte buf[];

    public Pack()
    {
        pk_head = new Header();
        buf = new byte[512];
    }


    public boolean read( DataInputStream in )
	throws IOException
    {
      pk_head.read(in);

      IOUtil.readBytes( in, buf, pk_head.pk_len - Header.HEADER_LEN );

      return true;
    }


}
