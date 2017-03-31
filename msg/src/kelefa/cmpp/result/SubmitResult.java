package com.kelefa.cmpp.result;

public class SubmitResult
    extends Result
{

  public SubmitResult()
  {
    msg_id = new byte[ 8 ];
    super.pack_id = 0x80000004;
  }

  /**
   * ��Ϣ��ʶ�������㷨���£�
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
   *
   * ��SP���������Ӧ����Ϣ��Sequence_Idһ���ԾͿɵõ�CMPP_Submit��Ϣ��Msg_Id��
   *
   */
  public byte[] msg_id;

  /**
   * ���
   * 0����ȷ
   * 1����Ϣ�ṹ��
   * 2�������ִ�
   * 3����Ϣ����ظ�
   * 4����Ϣ���ȴ�
   * 5���ʷѴ����
   * 6�����������Ϣ��
   * 7��ҵ������
   * 8���������ƴ�
   * 9~ ����������
   */
  public byte result;

  private static final String[] resultMsg = {
      "��ȷ", "��Ϣ�ṹ��", "�����ִ�", "��Ϣ����ظ�", "��Ϣ���ȴ�", "�ʷѴ����",
      "���������Ϣ��", "ҵ������", "�������ƴ�", "��������"
  };

  public String getResultMessage()
  {
    if ( result < 0 || result > 9 )
      result = 9;

    return resultMsg[ result ];
  }

}
