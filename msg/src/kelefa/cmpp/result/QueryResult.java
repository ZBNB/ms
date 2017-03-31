package com.kelefa.cmpp.result;

import java.io.DataInputStream;
import java.io.IOException;

public class QueryResult extends Result
{
  /** ʱ��(��ȷ����), len: 8 */
  public byte[] time = new byte[8];
  /**
   * ��ѯ���
   * 0��������ѯ
   * 1����ҵ�����Ͳ�ѯ
   */
  public byte type;
  /** ��ѯ��, len: 10 */
  public byte[] code = new byte[10];
  /** ��SP������Ϣ���� */
  public int mt_tlmsg;
  /** ��SP�����û����� */
  public int mt_tlusr;
  /** �ɹ�ת������ */
  public int mt_scs;
  /** ��ת������ */
  public int mt_wt;
  /** ת��ʧ������ */
  public int mt_fl;
  /** ��SP�ɹ��ʹ����� */
  public int mo_scs;
  /** ��SP���ʹ����� */
  public int mo_wt;
  /** ��SP�ʹ�ʧ������ */
  public int mo_fl;

  public QueryResult()
  {
    super.pack_id = 0x80000006;
  }

  public void read( DataInputStream in )
      throws IOException
  {
    in.read( time );
    type = in.readByte();
    in.read( code );

    mt_tlmsg = in.readInt();
    mt_tlusr = in.readInt();
    mt_scs = in.readInt();
    mt_wt = in.readInt();
    mt_fl = in.readInt();
    mo_scs = in.readInt();
    mo_wt = in.readInt();
    mo_fl = in.readInt();
  }

  protected void printAllField()
  {
    System.out.println( "time :" + time );
    System.out.println( "type :" + type );
    System.out.println( "code :" + code );
    System.out.println( "mt_tlmsg :" + mt_tlmsg );
    System.out.println( "mt_scs :" + mt_scs );
    System.out.println( "mt_tlusr :" + mt_tlusr );
    System.out.println( "mt_wt :" + mt_wt );
    System.out.println( "mt_fl :" + mt_fl );
    System.out.println( "mo_scs :" + mo_scs );
    System.out.println( "mo_wt :" + mo_wt );
  }
}
