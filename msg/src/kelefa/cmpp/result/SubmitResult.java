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
   * 信息标识，生成算法如下：
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
   *
   * （SP根据请求和应答消息的Sequence_Id一致性就可得到CMPP_Submit消息的Msg_Id）
   *
   */
  public byte[] msg_id;

  /**
   * 结果
   * 0：正确
   * 1：消息结构错
   * 2：命令字错
   * 3：消息序号重复
   * 4：消息长度错
   * 5：资费代码错
   * 6：超过最大信息长
   * 7：业务代码错
   * 8：流量控制错
   * 9~ ：其他错误
   */
  public byte result;

  private static final String[] resultMsg = {
      "正确", "消息结构错", "命令字错", "消息序号重复", "消息长度错", "资费代码错",
      "超过最大信息长", "业务代码错", "流量控制错", "其他错误"
  };

  public String getResultMessage()
  {
    if ( result < 0 || result > 9 )
      result = 9;

    return resultMsg[ result ];
  }

}
