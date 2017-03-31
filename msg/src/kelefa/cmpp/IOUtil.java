package com.kelefa.cmpp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IOUtil
{
  private IOUtil()
  {
  }

  public static boolean sendBytes( DataOutputStream out, byte[] bytes, int len )
      throws IOException
  {
    for ( int j = 0; j < len && j<bytes.length; j++ )
      out.writeByte( bytes[ j ] );

    out.flush();
    return true;
  }

  public static boolean sendBytes( DataOutputStream out, byte[] bytes )
      throws IOException
  {
    int i = 0;

    while ( bytes[ i ] != 0 && i < 200 ) {
      out.writeByte( bytes[ i ] );
      i++;
    }
    out.write( bytes[ i ] );
    out.flush();

    return true;
  }

  public static boolean readBytes( DataInputStream in, byte[] bytes, int len )
      throws IOException
  {
    for ( int j = 0; j < len; j++ )
      bytes[ j ] = in.readByte();

    return true;
  }

  public static int readBytes( DataInputStream in, byte[] bytes )
      throws IOException
  {
    int i = 0;

    do {
      bytes[ i ] = in.readByte();
      if ( bytes[ i ] == 0 )
	break;
      i++;
    }
    while ( true );

    return ++i;
  }
}
