package com.kelefa.cmpp;

import java.io.IOException;
import java.io.DataOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.DataInputStream;
import com.kelefa.cmpp.result.Result;
import com.kelefa.cmpp.result.QueryResult;


public final class Query
{
  public static final int QUERY_LEN = 8 + 1 + 10 + 8;

  /** ʱ��YYYYMMDD(��ȷ����) len: 8 */
  String time;
  /**
   * ��ѯ���
   * 0��������ѯ
   * 1����ҵ�����Ͳ�ѯ
   */
  byte type;
  /**
   * ��ѯ��
   * ��Query_TypeΪ0ʱ��������Ч��
   * ��Query_TypeΪ1ʱ��������дҵ������Service_Id.
   * len: 10
   */
  byte[] code;
  /** ���� len:8 */
  String reserve = "00000000";


  public Query()
  {
  }

  public void execute( ConnDesc conn )
      throws IOException
  {
    Header header = new Header();
    header.pk_len = QUERY_LEN + Header.HEADER_LEN;

    DataOutputStream out = new DataOutputStream( conn.sock.getOutputStream() );

    header.pk_cmd = Const.CMPPE_QUERY;
    header.pk_seq = conn.getSeq();
    header.send( out );

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    time = sdf.format(Calendar.getInstance().getTime());
    out.writeBytes(time);

    out.write(type);
    out.write(code);
    out.writeBytes(reserve);
  }

  public static Result respond(DataInputStream in,Header header)
      throws IOException
  {
    QueryResult queryResult = new QueryResult();

    queryResult.read( in );

    return queryResult;
  }

  public void setCode(String code)
  {
    this.code = new byte[10];
    if ( code == null || code.length() == 0 )
      return;
    byte[] tem = code.getBytes();
    System.arraycopy( tem, 0, this.code, 0, Math.min( tem.length, 10 ) );
  }

  public void setType( byte type )
  {
    this.type = type;
  }

}
