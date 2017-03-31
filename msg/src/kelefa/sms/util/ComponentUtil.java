package com.kelefa.sms.util;

import javax.servlet.ServletContext;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.interceptor.component.ComponentManager;

/**
 *
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: 天颐通信</p>
 * @author 杨杰荣
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

    if ( container == null && application != null ) { // 由quartz的任务线程调用，因为它不是一个request请求，上一步的container等于null
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
