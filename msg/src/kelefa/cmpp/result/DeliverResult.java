package com.kelefa.cmpp.result;

import java.io.PrintStream;

public final class DeliverResult extends Result
{
    public int seq;
    public byte src_addr[];
    public byte dst_addr[];
    public byte svc_type[];
    public byte proto_id;
    public byte status_rpt;
    public byte priority;
    public byte data_coding;
    public byte sm_len;
    public byte short_msg[];
    public byte msg_id[];
    public int status_from_rpt;
    public byte submit_time[];
    public byte done_time[];


    public DeliverResult()
    {
        msg_id = new byte[65];
        submit_time = new byte[13];
        done_time = new byte[13];
        src_addr = new byte[21];
        dst_addr = new byte[21];
        svc_type = new byte[11];
        short_msg = new byte[161];
        super.pack_id = 5;
    }

    protected void printAllField()
    {
        try
        {
            String s = new String(src_addr);
            System.out.println("src_addr:" + s);
            s = null;
            s = new String(dst_addr);
            System.out.println("dst_addr:" + s);
            s = null;
            s = new String(svc_type);
            System.out.println("svc_type:" + s);
            System.out.println("proto_id:" + proto_id);
            System.out.println("data_coding:" + data_coding);
            s = null;
            s = new String(short_msg);
            System.out.println("short_msg:" + s);
            if(status_rpt == 1)
            {
                System.out.println("status_from_rpt:" + status_from_rpt);
                String s1 = null;
                s1 = new String(submit_time);
                System.out.println("submit_time:" + s1);
                s1 = null;
                s1 = new String(done_time);
                System.out.println("done_time:" + s1);
            }
        }
        catch(Exception _ex)
        {
            return;
        }
    }

}
