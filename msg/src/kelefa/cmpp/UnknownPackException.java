package com.kelefa.cmpp;


public class UnknownPackException extends Exception
{

    public UnknownPackException()
    {
        details = "unknown packet is received";
    }

    String details;
}
