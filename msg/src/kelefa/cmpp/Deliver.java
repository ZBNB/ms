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
   * ��Ϣ��ʶ
   * �����㷨���£�
   * ����64λ��8�ֽڣ���������
   * ��1��	ʱ�䣨��ʽΪMMDDHHMMSS��������ʱ���룩��bit64~bit39������
   * bit64~bit61���·ݵĶ����Ʊ�ʾ��
   * bit60~bit56���յĶ����Ʊ�ʾ��
   * bit55~bit51��Сʱ�Ķ����Ʊ�ʾ��
   * bit50~bit45���ֵĶ����Ʊ�ʾ��
   * bit44~bit39����Ķ����Ʊ�ʾ��
   * ��2��	�������ش��룺bit38~bit17���Ѷ������صĴ���ת��Ϊ������д�����ֶ��С�
   * ��3��	���кţ�bit16~bit1��˳�����ӣ�����Ϊ1��ѭ��ʹ�á�
   * �������粻�����������㣬�Ҷ��롣
   */
  byte[] msg_Id = new byte[8];
  /**
   * Ŀ�ĺ���
   * SP�ķ�����룬һ��4--6λ��������ǰ׺Ϊ�������ĳ����룻�ú������ֻ��û�����Ϣ�ı��к��롣
   */
  byte[] dest_Id = new byte[21];
  /** ҵ�����ͣ������֡���ĸ�ͷ��ŵ���ϡ� */
  byte[] service_Id = new byte[10];
  /** GSMЭ�����͡���ϸ������ο�GSM03.40�е�9.2.3.9 */
  byte tp_pid;
  /** GSMЭ�����͡���ϸ������ο�GSM03.40�е�9.2.3.9 */
  byte tp_udhi;
  /**
   * ��Ϣ��ʽ
   *  0��ASCII��
   *  3������д������
   *  4����������Ϣ
   *  8��UCS2����
   *  15����GB����
   */
  byte msg_Fmt;
  /** Դ�ն�MSISDN���루״̬����ʱ��ΪCMPP_SUBMIT��Ϣ��Ŀ���ն˺��룩 */
  byte[] src_terminal_Id = new byte[21];
  /**
   * �Ƿ�Ϊ״̬����
   * 0����״̬����
   * 1��״̬����
   */
  byte registered_Delivery;
  /** ��Ϣ���� */
  int msg_Length;
  /** ��Ϣ���� */
  byte[] msg_Content;
  /** ������ */
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
