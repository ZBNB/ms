package com.kelefa.cmpp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import com.kelefa.cmpp.result.SubmitResult;

/**
 * CMPP_SUBMIT操作的目的是SP在与ISMG建立应用层连接后向ISMG提交短信。
 * ISMG以CMPP_SUBMIT_RESP消息响应。
 *
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: 天颐通信</p>
 * @author 杨杰荣
 * @version 1.0
 */
public class Submit
{
  private static final Logger log = Logger.getLogger(Submit.class);

  private long msgId;
  private byte pkTotal;
  private byte pkNumber;
  private byte registeredDelivery;
  private byte msgLevel;
  private byte[] serviceId;
  private byte feeUserType;
  private byte[] feeTerminalId;
  private byte tp_pId;
  /** GSM协议类型。详细是解释请参考GSM03.40中的9.2.3.23,仅使用1位，右对齐 */
  private byte tp_udhi;
  /**
   * 信息格式
   * 0：ASCII串
   * 3：短信写卡操作
   * 4：二进制信息
   * 8：UCS2编码
   * 15：含GB汉字
   */
  private byte msg_Fmt;
  /** 信息内容来源(SP_Id)，长度：6 */
  private String msg_src;
  /**
   * 资费类别，长度：2
   * 01：对“计费用户号码”免费
   * 02：对“计费用户号码”按条计信息费
   * 03：对“计费用户号码”按包月收取信息费
   * 04：对“计费用户号码”的信息费封顶
   * 05：对“计费用户号码”的收费是由SP实现
   */
  private String feeType;
  /** 资费代码（以分为单位），长度：6 */
  private String feeCode;
  /** 存活有效期，格式遵循SMPP3.3协议，长度：17 */
  private String valId_Time;
  /** 定时发送时间，格式遵循SMPP3.3协议，长度：17 */
  private String atTime;
  /**
   * 源号码
   * SP的服务代码或前缀为服务代码的长号码, 网关将该号码完整的填到SMPP协议Submit_SM消息相应
   * 的source_addr字段，该号码最终在用户手机上显示为短消息的主叫号码
   * 长度：21
   */
  private byte[] srcId;
  /** 接收信息的用户数量(小于100个用户) */
  private byte destUsr_tl;
  /** 接收短信的MSISDN号码，长度：21×N */
  private String[] dest_terminal_Id;
  /** 信息长度(Msg_Fmt值为0时：<160个字节；其它<=140个字节) */
  private byte msgLength;
  /** 长度：msgLength， 信息内容 */
  private byte[] msgContent;
  /** 长度：8 保留 */
  private String reserve;

  private int sequence = -1;
  /** 客户程序不要调用这个方法，com.kelefa.cmpp.mt.MtManager.send()专用 */
  public void setSequence(int sequence)
  {
    this.sequence = sequence;
  }
  public int getSequence()
  {
    return sequence;
  }


  public Submit()
  {
  }


  public void execute( ConnDesc conn )
      throws IOException
  {
    DataOutputStream out = new DataOutputStream( conn.sock.getOutputStream() );

    Header header = new Header();
    header.pk_len = getSubmitPackLen() + Header.HEADER_LEN;
    header.pk_cmd = Const.CMPPE_SUBMIT;
    header.pk_seq = sequence < 0 ? conn.getSeq() : sequence;
    header.send( out );

    out.writeLong(msgId);
    out.writeByte(pkTotal);
    out.writeByte(pkNumber);
    out.writeByte(registeredDelivery);
    out.writeByte(msgLevel);

    out.write(serviceId);

    out.writeByte(feeUserType);
    out.write(feeTerminalId);
    out.writeByte(tp_pId);
    out.writeByte(tp_udhi);
    out.writeByte(msg_Fmt);
    out.writeBytes(msg_src);
    out.writeBytes(feeType);
    out.writeBytes(feeCode);

    byte[] b_valid_time = new byte[17];
    out.write(b_valid_time); // valId_Time
    out.write(b_valid_time); // atTime

    out.write(srcId);

    out.writeByte(destUsr_tl);
    log.debug("发送短信给:");
    for (int i = 0; i < dest_terminal_Id.length; i++)
    {
      byte[] b_dest_terminal_Id = new byte[ 21 ];
      byte[] tem = dest_terminal_Id[i].getBytes( "gb2312" );
      System.arraycopy( tem, 0, b_dest_terminal_Id, 0, tem.length );
      out.write( b_dest_terminal_Id );
      log.debug(dest_terminal_Id[i]);
    }

    out.writeByte(msgLength);
    out.write(msgContent);

    byte[] reserve = new byte[8];
    out.write( reserve ); // reserve

    log.debug("短信内容:"+new String(msgContent));
  }

  public static SubmitResult respond(DataInputStream in,Header header)
      throws IOException
  {
    SubmitResult submitResult = new SubmitResult();

    in.read( submitResult.msg_id );
    submitResult.result = in.readByte();

    return submitResult;
  }


  private int getSubmitPackLen()
  {
    int msgLen = msgLength < 0 ? 256 + msgLength : msgLength;
    return 8 + 1 + 1 + 1 + 1 + 10 + 1 + 21 + 1 + 1 + 1 + 6 + 2 + 6 + 17 + 17 +
	21 + 1 + 21 * destUsr_tl + 1 + msgLen + 8;
  }

  public void setMsgId( long msgId )
  {
    this.msgId = msgId;
  }

  public void setPkTotal( byte pkTotal )
  {
    this.pkTotal = pkTotal;
  }

  public void setPkNumber( byte pkNumber )
  {
    this.pkNumber = pkNumber;
  }

  public void setRegisteredDelivery( byte registeredDelivery )
  {
    this.registeredDelivery = registeredDelivery;
  }

  public void setMsgLevel( byte msgLevel )
  {
    this.msgLevel = msgLevel;
  }

  public void setServiceId( String serviceId )
  {
    try {
      this.serviceId = new byte[ 10 ];
      byte[] tem = serviceId.getBytes( "gb2312" );
      System.arraycopy( tem, 0, this.serviceId, 0, tem.length );
    }
    catch ( UnsupportedEncodingException ex ) {
    }
  }

  public void setFeeUserType( byte feeUserType )
  {
    this.feeUserType = feeUserType;
  }

  public void setFeeTerminalId( String feeTerminalId )
  {
    try {
      this.feeTerminalId = new byte[ 21 ];
      byte[] tem = feeTerminalId.getBytes( "gb2312" );
      System.arraycopy( tem, 0, this.feeTerminalId, 0, tem.length );
    }
    catch ( UnsupportedEncodingException ex ) {
    }
  }

  public void setTp_pId( byte tp_pId )
  {
    this.tp_pId = tp_pId;
  }

  public void setTp_udhi( byte tp_udhi )
  {
    this.tp_udhi = tp_udhi;
  }

  public void setMsg_Fmt( byte msg_Fmt )
  {
    this.msg_Fmt = msg_Fmt;
  }

  public void setMsg_src( String msg_src )
  {
    this.msg_src = msg_src;
  }

  public void setFeeType( String feeType )
  {
    this.feeType = feeType;
  }

  public void setValId_Time( String valId_Time )
  {
    this.valId_Time = valId_Time;
  }

  public void setAtTime( String atTime )
  {
    this.atTime = atTime;
  }

  public void setSrcId( String srcId )
  {
    try {
      this.srcId = new byte[ 21 ];
      byte[] tem = srcId.getBytes( "gb2312" );
      System.arraycopy( tem, 0, this.srcId, 0, tem.length );
    }
    catch ( UnsupportedEncodingException ex ) {
    }

  }

  public void setDestUsr_tl( byte destUsr_tl )
  {
    this.destUsr_tl = destUsr_tl;
  }

  public void setDest_terminal_Id( String[] dest_terminal_Id )
  {
    this.dest_terminal_Id = dest_terminal_Id;
  }

  public void setMsgContent( String msgContent )
  {
    try {
      byte[] tmp = msgContent.getBytes( "gb2312" );
      if ( tmp.length <= 140 )
      {
	this.msgContent = tmp;
	this.msgLength = ( byte )tmp.length;
      }
      else
      {
	this.msgContent = new byte[ 140 ];
	System.arraycopy( tmp, 0, this.msgContent, 0, 140 );
	this.msgLength = (byte)140;
      }
    }
    catch ( UnsupportedEncodingException ex ) {
    }
  }

  public void setReserve( String reserve )
  {
    this.reserve = reserve;
  }

  public void setFeeCode( String feeCode )
  {
    this.feeCode = feeCode;
  }
}
