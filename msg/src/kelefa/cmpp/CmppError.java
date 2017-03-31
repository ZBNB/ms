package com.kelefa.cmpp;


public class CmppError
{

    public CmppError()
    {
    }

    public static final int CMPP_SUCCESS = 0;
    public static final int CMPP_ERR_START = 1000;
    public static final int CMPP_OS_ERR_START = 1500;
    public static final int CMPP_START = 2000;
    public static final int CMPP_USR_ERR_START = 2500;
    public static final int CMPP_EINVALSOCK = 1001;
    public static final int CMPP_HOST_UNREACHABLE = 1002;
    public static final int CMPP_EOF = 2001;
    public static final int CMPP_NOIMPL = 2002;
    public static final int CMPP_EXCEED = 2003;
    public static final int CMPP_QUEUE_FULL = 2004;
    public static final int CMPP_QUEUE_EMPTY = 2005;
    public static final int CMPP_ARRAY_OUTBOUND = 2006;
    public static final int CMPP_STACK_EMPTY = 2007;
    public static final int CMPP_INCOMPLETE = 2008;
    public static final int CMPP_CMPPE_ERROR = 2009;
}
