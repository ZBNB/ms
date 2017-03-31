package com.kelefa.sms.guohai;

public class SmsMo
{
  private Long id;
  private Long msg_id;
  private String cp_id;
  private Integer registered_delivery;
  private String destination_id;
  private String msg_content;
  private String dest_terminal_id;
  private String ih_timestamp;
  private String exp1;
  private String exp2;
  private String exp3;

  public SmsMo()
  {
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public void setMsg_id( Long msg_id )
  {
    this.msg_id = msg_id;
  }

  public void setCp_id( String cp_id )
  {
    this.cp_id = cp_id;
  }

  public void setRegistered_delivery( Integer registered_delivery )
  {
    this.registered_delivery = registered_delivery;
  }

  public void setDestination_id( String destination_id )
  {
    this.destination_id = destination_id;
  }

  public void setMsg_content( String msg_content )
  {
    this.msg_content = msg_content;
  }

  public void setDest_terminal_id( String dest_terminal_id )
  {
    this.dest_terminal_id = dest_terminal_id;
  }

  public void setIh_timestamp( String ih_timestamp )
  {
    this.ih_timestamp = ih_timestamp;
  }

  public void setExp1( String exp1 )
  {
    this.exp1 = exp1;
  }

  public void setExp2( String exp2 )
  {
    this.exp2 = exp2;
  }

  public void setExp3( String exp3 )
  {
    this.exp3 = exp3;
  }

  public Long getId()
  {
    return id;
  }

  public Long getMsg_id()
  {
    return msg_id;
  }

  public String getCp_id()
  {
    return cp_id;
  }

  public Integer getRegistered_delivery()
  {
    return registered_delivery;
  }

  public String getDestination_id()
  {
    return destination_id;
  }

  public String getMsg_content()
  {
    return msg_content;
  }

  public String getDest_terminal_id()
  {
    return dest_terminal_id;
  }

  public String getIh_timestamp()
  {
    return ih_timestamp;
  }

  public String getExp1()
  {
    return exp1;
  }

  public String getExp2()
  {
    return exp2;
  }

  public String getExp3()
  {
    return exp3;
  }

}
