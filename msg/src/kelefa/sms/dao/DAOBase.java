package com.kelefa.sms.dao;

import java.io.Serializable;
import java.util.List;

import com.kelefa.sms.util.HibernateUtil;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

public abstract class DAOBase implements AbstractDAO
{
  public DAOBase()
  {
  }

  public void updateRec(Object newOne) throws Exception
  {
    Session session = HibernateUtil.currentSession();
    Transaction tx = session.beginTransaction();

    try {
      session.update( newOne );
      tx.commit();
    }
    catch ( HibernateException ex ) {
      tx.rollback();
      throw ex;
    }
    finally{
      HibernateUtil.closeSession();
    }
  }

  public void insertRec(Object newOne)
      throws Exception
  {
    Session session = HibernateUtil.currentSession();
    Transaction tx = session.beginTransaction();

    try {
      session.save(newOne);
      tx.commit();
    }
    catch (Exception ex) {
      tx.rollback();
      throw ex;
    }
    finally {
      HibernateUtil.closeSession();
    }

  }

  protected void deleteRec(String sql)
      throws Exception
  {
    Session session = HibernateUtil.currentSession();
    Transaction tx = session.beginTransaction();

    try {
      session.delete( sql );
      tx.commit();
    }
    catch (Exception ex) {
      tx.rollback();
      throw ex;
    }
    finally {
      HibernateUtil.closeSession();
    }

  }

  protected Object load(Class theClass, Serializable id) throws Exception
  {
    Session session = HibernateUtil.currentSession();
    Transaction tx = session.beginTransaction();

    Object obj = null;
    try {
      obj = session.load( theClass, id );
      tx.commit();
    }
    catch ( Exception ex ) {
      tx.rollback();
      throw ex;
    }
    finally {
      HibernateUtil.closeSession();
    }

    return obj;
  }

  protected List selectAllRec(int pageno, int pagelist,String sql)
      throws Exception
  {
    Session session = HibernateUtil.currentSession();
    Transaction tx = session.beginTransaction();

    List page = null;
    try {
      Query query = session.createQuery( sql );
      query.setMaxResults( pagelist );
      int beginIndex = ( pageno - 1 ) * pagelist;
      if ( beginIndex < 0 ) {
	beginIndex = 0;
      }
      query.setFirstResult( beginIndex );

      page = query.list();

      tx.commit();
    }
    catch ( HibernateException ex ) {
      tx.rollback();
      throw ex;
    }
    finally {
      HibernateUtil.closeSession();
    }

    return page;
  }

  protected int selectAllRecCount(String sql) throws Exception
  {
    Session session = HibernateUtil.currentSession();
    Transaction tx = session.beginTransaction();

    int total = 0;
    try {
//      Query query = session.createQuery( sql );
      total = ( ( Integer ) session.iterate(sql).next() ).intValue();

      tx.commit();
    }
    catch ( HibernateException ex ) {
      tx.rollback();
      throw ex;
    }
    finally {
      HibernateUtil.closeSession();
    }

    return total;
  }

  protected List searchRecByFilter(String filter, int pageno, int pagelist,
				String sql, Class theClass)
      throws Exception
  {
    Session session = HibernateUtil.currentSession();
    Transaction tx = session.beginTransaction();

    List page = null;
    try {
      if ( filter != null && filter.length() != 0 ) {
	sql += " where " + filter;
      }
      Query query = session.createSQLQuery( sql, "result", theClass );
      query.setMaxResults( pagelist );
      int beginIndex = ( pageno - 1 ) * pagelist;
      if ( beginIndex < 0 ) {
	beginIndex = 0;
      }
      query.setFirstResult( beginIndex );

      page = query.list();

      tx.commit();
    }
    catch ( HibernateException ex ) {
      tx.rollback();
      throw ex;
    }
    finally {
      HibernateUtil.closeSession();
    }

    return page;
  }

  protected int searchRecByFilterCount(String filter,String sql)
      throws Exception
  {
    if (filter != null && filter.length() != 0)
      sql += " where " + filter;

    Session session = HibernateUtil.currentSession();
    Transaction tx = session.beginTransaction();

    int total = 0;
    try {
//      Query query = session.createQuery( sql );
      total = ( ( Integer ) session.iterate(sql).next() ).intValue();

      tx.commit();
    }
    catch ( HibernateException ex ) {
      tx.rollback();
      throw ex;
    }
    finally {
      HibernateUtil.closeSession();
    }

    return total;
  }
}
