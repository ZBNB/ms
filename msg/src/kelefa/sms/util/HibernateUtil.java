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
   * �Ȳ�close session����request����ǰ���߳̽�����close��������closeSession2()��
   * �޸ģ�
   * 1. ��ӹ�������com.skylink.greenChannel.filter.HibernateSessionFilter,
   *     ��request����ǰ����closeSession2()
   * 2. �޸�com.skylink.greenChannel.job.SmsHandler.execute(),�ں����˳�ǰ��
   *    ����������߳�ǰ����closeSession2()
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
