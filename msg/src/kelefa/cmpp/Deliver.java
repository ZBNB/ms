package com.kelefa.cmpp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

public class Deliver implements java.io.Serializable
{
  private static final Logger log = Logger.getLogger(Deliver.class);

  /**
   * 信息标识
   * 生成算法如下：
   * 采用64位（8字节）的整数：
   * （1）	时间（格式为MMDDHHMMSS，即月日时分秒）：bit64~bit39，其中
   * bit64~bit61：月份的二进制表示；
   * bit60~bit56：日的二进制表示；
   * bit55~bit51：小时的二进制表示；
   * bit50~bit45：分的二进制表示；
   * bit44~bit39：秒的二进制表示；
   * （2）	短信网关代码：bit38~bit17，把短信网关的代码转换为整数填写到该字段中。
   * （3）	序列号：bit16~bit1，顺序增加，步长为1，循环使用。
   * 各部分如不能填满，左补零，右对齐。
   */
  byte[] msg_Id = new byte[8];
  /**
   * 目的号码
   * SP的服务代码，一般4--6位，或者是前缀为服务代码的长号码；该号码是手机用户短消息的被叫号码。
   */
  byte[] dest_Id = new byte[21];
  /** 业务类型，是数字、字母和符号的组合。 */
  byte[] service_Id = new byte[10];
  /** GSM协议类型。详细解释请参考GSM03.40中的9.2.3.9 */
  byte tp_pid;
  /** GSM协议类型。详细解释请参考GSM03.40中的9.2.3.9 */
  byte tp_udhi;
  /**
   * 信息格式
   *  0：ASCII串
   *  3：短信写卡操作
   *  4：二进制信息
   *  8：UCS2编码
   *  15：含GB汉字
   */
  byte msg_Fmt;
  /** 源终端MSISDN号码（状态报告时填为CMPP_SUBMIT消息的目的终端号码） */
  byte[] src_terminal_Id = new byte[21];
  /**
   * 是否为状态报告
   * 0：非状态报告
   * 1：状态报告
   */
  byte registered_Delivery;
  /** 消息长度 */
  int msg_Length;
  /** 消息内容 */
  byte[] msg_Content;
  /** 保留项 */
  byte[] reserved = new byte[8];


  public Deliver()
  {
  }


  public void execute( DataInputStream in,DataOutputStream out, Header header )
      throws IOException
  {
    log.debug("reading Deliver...");
    in.read(msg_Id);
    in.read(dest_Id);
    log.debug("dest_Id="+new String(dest_Id));
    in.read(service_Id);
    log.debug("service_Id="+new String(service_Id));
    tp_pid = in.readByte();
    tp_udhi = in.readByte();
    msg_Fmt = in.readByte();
    log.debug("msg_Fmt="+msg_Fmt);
    in.read(src_terminal_Id);
    registered_Delivery = in.readByte();
    byte tmpLen = in.readByte();
    msg_Length = tmpLen;
    if (msg_Length<0) msg_Length += 256;
    log.debug("msg_Length="+msg_Length);
    msg_Content = new byte[msg_Length];
    in.read(msg_Content);
    log.debug("msg_Content:" + getMsg_Content());
    in.read(reserved);


    // response

    Header responseHeader = new Header();
    responseHeader.pk_cmd = Const.CMPPE_DELIVER_RESP;
    responseHeader.pk_len += 9;
    responseHeader.pk_seq = header.pk_seq;
    responseHeader.send(out);

    out.write(msg_Id);
    out.writeByte(0);
  }

  public String getMsg_Content()
  {
    try {
    if ( msg_Fmt == 8 )
      return new String( msg_Content, 0, msg_Length, "UnicodeBigUnmarked" );
    else
      return new String( msg_Content, 0, msg_Length, "gb2312" );
    }
    catch ( UnsupportedEncodingException ex ) {
      ex.printStackTrace();
      return "";
    }
  }

  public String getTerminal_Id()
  {
    return new String( src_terminal_Id,0,16 );
  }

  public long getMsg_Id()
  {
    long v = 0;
    for (int i = 0; i < 8; i++)
    {
      v <<= 1;
      v += msg_Id[7-i];
    }
    return v ;
  }

  public String getDest_Id()
  {
    return new String( dest_Id,0,10 );
  }

  public String getService_Id()
  {
    return new String( service_Id );
  }

  public byte getMsg_Fmt()
  {
    return msg_Fmt;
  }

  public String getSrc_terminal_Id()
  {
    return new String( src_terminal_Id );
  }

  public byte getRegistered_Delivery()
  {
    return registered_Delivery;
  }
}
