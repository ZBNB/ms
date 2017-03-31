package com.kelefa.cmpp.mo;

import com.kelefa.cmpp.Deliver;

/**
 * 处理接收到的短信
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: 天颐通信</p>
 * @author 杨杰荣
 * @version 1.0
 */
public interface DeliverHandler extends java.io.Serializable
{
  public void handle(Deliver deliver);
}
