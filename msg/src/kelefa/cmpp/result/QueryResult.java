package com.kelefa.cmpp.result;

import java.io.DataInputStream;
import java.io.IOException;

public class QueryResult extends Result
{
  /** 时间(精确至日), len: 8 */
  public byte[] time = new byte[8];
  /**
   * 查询类别
   * 0：总数查询
   * 1：按业务类型查询
   */
  public byte type;
  /** 查询码, len: 10 */
  public byte[] code = new byte[10];
  /** 从SP接收信息总数 */
  public int mt_tlmsg;
  /** 从SP接收用户总数 */
  public int mt_tlusr;
  /** 成功转发数量 */
  public int mt_scs;
  /** 待转发数量 */
  public int mt_wt;
  /** 转发失败数量 */
  public int mt_fl;
  /** 向SP成功送达数量 */
  public int mo_scs;
  /** 向SP待送达数量 */
  public int mo_wt;
  /** 向SP送达失败数量 */
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
