package com.kelefa.sms.dao;

import java.io.Serializable;
import com.kelefa.sms.guohai.SmsMt;
import java.util.List;

public class SmsMtDAO extends DAOBase implements Serializable
{
  public SmsMtDAO()
  {}

  public void deleteRec(long id)
      throws Exception
  {
    deleteRec ("from GHZQ_smsMT_Table in class SmsMt where GHZQ_smsMT_Table.id=" + id);
  }

  public void deleteRec(int id)
      throws Exception
  {
    deleteRec ("from GHZQ_smsMT_Table in class SmsMt where GHZQ_smsMT_Table.id=" + id);
  }

  public Object selectRecByPrimaryKey(long id)
      throws Exception
  {
    Object obj = load(SmsMt.class, new Long(id));
    return obj;
  }

  public Object selectRecByPrimaryKey(int id)
      throws Exception
  {
    Object obj = load(SmsMt.class, new Integer(id));
    return obj;
  }

  public List selectAllRec(int pageno, int pagelist)
      throws Exception
  {
    String sql = "from GHZQ_smsMT_Table in class SmsMt";
    return selectAllRec(pageno, pagelist, sql);
  }

  public int selectAllRecCount()
      throws Exception
  {
    String sql = "SELECT count(*) FROM SmsMt GHZQ_smsMT_Table";
    return selectAllRecCount(sql);
  }

  public List searchRecByFilter(String filter, int pageno, int pagelist)
      throws Exception
  {
    String sql = "select {result.*} from GHZQ_smsMT_Table {result}";
    return searchRecByFilter(filter,pageno,pagelist,sql,SmsMt.class);
  }

  public int searchRecByFilterCount(String filter)
      throws Exception
  {
    String sql = "select count(*) from SmsMt GHZQ_smsMT_Table";
    return searchRecByFilterCount(filter,sql);
  }
}
