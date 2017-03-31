package com.kelefa.sms.action;

import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionSupport;

public class Login
    extends ActionSupport
{
  private String account;
  private String password;
  private String errorMsg;

  public Login()
  {
  }

  public String execute()
      throws Exception
  {
    if ( password == null || password.length() == 0 ||
	 account == null || account.length() == 0 )
    {
      errorMsg = ( "’ ∫≈∫Õ√‹¬Î≤ªƒ‹Œ™ø’" );
      return Action.LOGIN;
    }


    if ( !"skylink".equals( password ) || !"admin".equals( account ) ) {
      errorMsg = ( "√‹¬Î¥ÌŒÛ" );
      return Action.LOGIN;
    }

    ActionContext.getContext().getSession().put( "login", "true" );
    return Action.SUCCESS;

  }

  public void setAccount( String account )
  {
    this.account = account;
  }

  public void setPassword( String password )
  {
    this.password = password;
  }

  public String getAccount()
  {
    return account;
  }

  public String getPassword()
  {
    return password;
  }

  public String getErrorMsg()
  {
    return errorMsg;
  }

}
