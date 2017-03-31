package com.kelefa.sms.util;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;

public class HibernateUtil
{
  public HibernateUtil()
  {
  }

  private static final SessionFactory sessionFactory;

  static
  {
    try {
      // Create the SessionFactory
      Configuration cfg = new Configuration()
	  .configure( "/hibernate.cfg.xml" );
      sessionFactory = cfg.buildSessionFactory();
    }
    catch ( Throwable ex ) {
      ex.printStackTrace();
      throw new ExceptionInInitializerError( ex );
    }
  }

  public static final ThreadLocal session = new ThreadLocal();

  public static Session currentSession()
      throws HibernateException
  {
    Session s = ( Session ) session.get();
    // Open a new Session, if this Thread has none yet
    if ( s == null ) {
      s = sessionFactory.openSession();
      session.set( s );
    }
    return s;
  }

  /**
   * 先不close session，等request结束前或线程结束才close，即调用closeSession2()。
   * 修改：
   * 1. 添加过滤器：com.skylink.greenChannel.filter.HibernateSessionFilter,
   *     在request结束前调用closeSession2()
   * 2. 修改com.skylink.greenChannel.job.SmsHandler.execute(),在函数退出前，
   *    即结束这个线程前调用closeSession2()
   * @throws HibernateException
   */
  public static void closeSession()
      throws HibernateException
  {
//		Session s = ( Session ) session.get();
//		session.set( null );
//		if ( s != null )
//			s.close();
  }

  public static void closeSession2()
      throws HibernateException
  {
    Session s = ( Session ) session.get();
    session.set( null );
    if ( s != null ) {
      s.close();
    }
  }
}
