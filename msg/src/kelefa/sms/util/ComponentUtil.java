package com.kelefa.sms.util;

import javax.servlet.ServletContext;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.interceptor.component.ComponentManager;

/**
 *
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: ����ͨ��</p>
 * @author �����
 * @version 1.0
 */
public class ComponentUtil
{
  public transient static ServletContext application;

  public static Object initializeObject( Object obj )
  {
    ComponentManager container =
	( ComponentManager ) ActionContext.getContext().get(
	"com.opensymphony.xwork.interceptor.component.ComponentManager" );

    if ( container == null && application != null ) { // ��quartz�������̵߳��ã���Ϊ������һ��request������һ����container����null
      container = ( ComponentManager )
	  application.getAttribute( ComponentManager.COMPONENT_MANAGER_KEY );
    }

    if ( container != null ) {
      container.initializeObject( obj );
    }

    if ( container == null )
      System.err.println( "can't get ComponentManager, " +
			  obj.getClass().toString() );

    return obj;
  }
}
