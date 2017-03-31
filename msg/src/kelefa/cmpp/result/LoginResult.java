package com.kelefa.cmpp.result;

import com.kelefa.cmpp.Const;

public class LoginResult extends Result
{

    public LoginResult()
    {
        auth = new byte[17];
        super.pack_id = Const.CMPPE_LOGIN_RESP;
    }

    public byte auth[];
    public byte version;
}
