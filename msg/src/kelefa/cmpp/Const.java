package com.kelefa.cmpp;

import java.io.*;

public class Const
{
  public static final byte CMPPE_MAX_ICP_ID_LEN = 6;
  public static final byte CMPPE_AUTH_LEN = 16;
  public static final byte CMPPE_MAX_SVC_LEN = 10;
  public static final byte CMPPE_DATETIME_LEN = 16;
  public static final byte CMPPE_MAX_DSTS_NUM = 100;
  public static final byte CMPPE_MAX_MSGID_LEN = 64;
  public static final byte CMPPE_MAX_MSISDN_LEN = 20;
  public static final short CMPPE_MAX_SM_LEN = 160;
  public static final byte CMPPE_DATE_TIME_LEN = 14;
  public static final byte CMPPE_MAX_FEE_TYPE = 99;
  public static final int CMPPE_MAX_INFO_FEE = 0xf423f;
  public static final byte CMPPE_MAX_PRIORITY = 9;
  public static final byte CMPPE_QUERY_TIME_LEN = 8;
  public static final byte CMPPE_QUERY_CODE_LEN = 10;
  public static final byte CMPPE_PK_HEAD_SIZE = 16;
  public static final byte CMPPE_PK_LEN_SIZE = 4;
  public static final byte CMPPE_MIN_PK_SIZE = 16;
  public static final short CMPPE_MAX_PK_SIZE = 2477;
  public static final byte CMPPE_MM_NO_REGIST = 0;
  public static final byte CMPPE_MM_REGIST = 1;
  public static final byte CMPPE_MM_CONTROL = 2;
  public static final byte CMPPE_BIND_SEND = 0;
  public static final byte CMPPE_BIND_RECV = 1;
  public static final byte CMPPE_DC_ASCII = 0;
  public static final byte CMPPE_DC_STK = 3;
  public static final byte CMPPE_DC_BIN = 4;
  public static final byte CMPPE_DC_UCS2 = 8;
  public static final byte CMPPE_DC_GB2312 = 15;
  public static final int CMPPE_NACK_RESP = 0x80000000;
  public static final int CMPPE_LOGIN = 1;
  public static final int CMPPE_LOGIN_RESP = 0x80000001;
  public static final int CMPPE_LOGOUT = 2;
  public static final int CMPPE_LOGOUT_RESP = 0x80000002;
  public static final int CMPPE_ROUTE = 3;
  public static final int CMPPE_ROUTE_RESP = 0x80000003;
  public static final int CMPPE_SUBMIT = 4;
  public static final int CMPPE_SUBMIT_RESP = 0x80000004;
  public static final int CMPPE_DELIVER = 5;
  public static final int CMPPE_DELIVER_RESP = 0x80000005;
  public static final int CMPPE_QUERY = 6;
  public static final int CMPPE_QUERY_RESP = 0x80000006;
  public static final int CMPPE_CANCEL = 7;
  public static final int CMPPE_CANCEL_RESP = 0x80000007;
  public static final int CMPPE_ACTIVE = 8;
  public static final int CMPPE_ACTIVE_RESP = 0x80000008;
  public static final int CMPPE_FORWORD = 9;
  public static final int CMPPE_FORWORD_RESP = 0x80000009;
  public static final int CMPPE_SUBMIT_M = 10;
  public static final int CMPPE_SUBMIT_M_RESP = 0x8000000a;
  public static final int CMPPE_RSP_SUCCESS = 0;
  public static final int CMPPE_RSP_OTHER_ERR = 1;
  public static final int CMPPE_RSP_INVAL_MSG_LEN = 2;
  public static final int CMPPE_RSP_UNKNOWN_CMD = 3;
  public static final int CMPPE_RSP_SYNC_ERR = 4;
  public static final int CMPPE_RSP_INVAL_STRUCT = 5;
  public static final int CMPPE_RSP_INVAL_ICP = 16;
  public static final int CMPPE_RSP_INVAL_AUTH = 17;
  public static final int CMPPE_RSP_INVAL_BIND_TYPE = 18;
  public static final int CMPPE_RSP_BINDED = 19;
  public static final int CMPPE_RSP_BIND_EXCEED = 20;
  public static final int CMPPE_RSP_NOT_BIND = 21;
  public static final int CMPPE_RSP_INVAL_MSG_MODE = 32;
  public static final int CMPPE_RSP_INVAL_DATA_CODING = 33;
  public static final int CMPPE_RSP_INVAL_SVC_TYPE = 34;
  public static final int CMPPE_RSP_INVAL_FEE_TYPE = 35;
  public static final int CMPPE_RSP_INVAL_DATETIME = 36;
  public static final int CMPPE_RSP_DSTS_EXCEED = 37;
  public static final int CMPPE_RSP_SMLEN_EXCEED = 38;
  public static final int CMPPE_RSP_INVAL_MSISDN = 38;
  public static final int CMPPE_RSP_INVAL_PARA = 39;
  public static final int CMPPE_RSP_PK_SEQ_REPEAT = 48;
  public static final int CMPPE_RSP_PK_SEQ_EXCEED = 49;
  public static final int CMPPE_RSP_MSG_NOT_FOUND = 50;
  public static final int CMPPE_RSP_LEN_BAD = 136;

  public Const()
  {
  }
}
