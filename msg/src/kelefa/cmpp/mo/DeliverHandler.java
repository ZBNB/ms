package com.kelefa.cmpp.mo;

import com.kelefa.cmpp.Deliver;

/**
 * ������յ��Ķ���
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: ����ͨ��</p>
 * @author �����
 * @version 1.0
 */
public interface DeliverHandler extends java.io.Serializable
{
  public void handle(Deliver deliver);
}
