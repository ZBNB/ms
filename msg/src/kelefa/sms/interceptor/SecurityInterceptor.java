package com.kelefa.sms.interceptor;

import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.ActionSupport;
import com.opensymphony.xwork.interceptor.Interceptor;

public class SecurityInterceptor
    implements Interceptor

{
  public SecurityInterceptor()
  {
  }

  public void destroy()
  {
  }

  public void init()
  {
  }

  public String intercept( ActionInvocation invocation )
      throws Exception
  {
    if ( !isAuthorized( invocation ) ) {
      return Action.LOGIN;
    }

    return invocation.invoke();
  }

  protected boolean isAuthorized( ActionInvocation actionInvocation )
  {
    boolean logined = "true".equals( actionInvocation
	.getInvocationContext()
	.getSession()
	.get( "login" ) );


    if ( !logined ) {
      ActionSupport action = ( ActionSupport ) actionInvocation.getAction();
      action.addActionError( "ÇëÄãÏÈµÇÂ½" );

      return false;
    }

    return true;
  }

}
