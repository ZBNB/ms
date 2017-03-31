package com.kelefa.cmpp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import com.kelefa.cmpp.result.SubmitResult;

/**
 * CMPP_SUBMIT������Ŀ����SP����ISMG����Ӧ�ò����Ӻ���ISMG�ύ���š�
 * ISMG��CMPP_SUBMIT_RESP��Ϣ��Ӧ��
 *
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: ����ͨ��</p>
 * @author �����
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
  /** GSMЭ�����͡���ϸ�ǽ�����ο�GSM03.40�е�9.2.3.23,��ʹ��1λ���Ҷ��� */
  private byte tp_udhi;
  /**
   * ��Ϣ��ʽ
   * 0��ASCII��
   * 3������д������
   * 4����������Ϣ
   * 8��UCS2����
   * 15����GB����
   */
  private byte msg_Fmt;
  /** ��Ϣ������Դ(SP_Id)�����ȣ�6 */
  private String msg_src;
  /**
   * �ʷ���𣬳��ȣ�2
   * 01���ԡ��Ʒ��û����롱���
   * 02���ԡ��Ʒ��û����롱��������Ϣ��
   * 03���ԡ��Ʒ��û����롱��������ȡ��Ϣ��
   * 04���ԡ��Ʒ��û����롱����Ϣ�ѷⶥ
   * 05���ԡ��Ʒ��û����롱���շ�����SPʵ��
   */
  private String feeType;
  /** �ʷѴ��루�Է�Ϊ��λ�������ȣ�6 */
  private String feeCode;
  /** �����Ч�ڣ���ʽ��ѭSMPP3.3Э�飬���ȣ�17 */
  private String valId_Time;
  /** ��ʱ����ʱ�䣬��ʽ��ѭSMPP3.3Э�飬���ȣ�17 */
  private String atTime;
  /**
   * Դ����
   * SP�ķ�������ǰ׺Ϊ�������ĳ�����, ���ؽ��ú����������SMPPЭ��Submit_SM��Ϣ��Ӧ
   * ��source_addr�ֶΣ��ú����������û��ֻ�����ʾΪ����Ϣ�����к���
   * ���ȣ�21
   */
  private byte[] srcId;
  /** ������Ϣ���û�����(С��100���û�) */
  private byte destUsr_tl;
  /** ���ն��ŵ�MSISDN���룬���ȣ�21��N */
  private String[] dest_terminal_Id;
  /** ��Ϣ����(Msg_FmtֵΪ0ʱ��<160���ֽڣ�����<=140���ֽ�) */
  private byte msgLength;
  /** ���ȣ�msgLength�� ��Ϣ���� */
  private byte[] msgContent;
  /** ���ȣ�8 ���� */
  private String reserve;

  private int sequence = -1;
  /** �ͻ�����Ҫ�������������com.kelefa.cmpp.mt.MtManager.send()ר�� */
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
    log.debug("���Ͷ��Ÿ�:");
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

    log.debug("��������:"+new String(msgContent));
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
