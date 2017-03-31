package com.kelefa.sms.dao;

import java.io.Serializable;
import com.kelefa.sms.guohai.SmsMo;
import java.util.List;

public class SmsMoDAO extends DAOBase implements Serializable
{
  public SmsMoDAO()
  {
  }
  public void deleteRec(long id)
      throws Exception
  {
    deleteRec ("from GHZQ_smsMO_Table in class SmsMo where GHZQ_smsMO_Table.id=" + id);
  }

  public void deleteRec(int id)
      throws Exception
  {
    deleteRec ("from GHZQ_smsMO_Table in class SmsMo where GHZQ_smsMO_Table.id=" + id);
  }

  public Object selectRecByPrimaryKey(long id)
      throws Exception
  {
    Object obj = load(SmsMo.class, new Long(id));
    return obj;
  }

  public Object selectRecByPrimaryKey(int id)
      throws Exception
  {
    Object obj = load(SmsMo.class, new Integer(id));
    return obj;
  }

  public List selectAllRec(int pageno, int pagelist)
      throws Exception
  {
    String sql = "from GHZQ_smsMO_Table in class SmsMo";
    return selectAllRec(pageno, pagelist, sql);
  }

  public int selectAllRecCount()
      throws Exception
  {
    String sql = "SELECT count(*) FROM SmsMo GHZQ_smsMO_Table";
    return selectAllRecCount(sql);
  }

  public List searchRecByFilter(String filter, int pageno, int pagelist)
      throws Exception
  {
    String sql = "select {result.*} from GHZQ_smsMO_Table {result}";
    return searchRecByFilter(filter,pageno,pagelist,sql,SmsMo.class);
  }

  public int searchRecByFilterCount(String filter)
      throws Exception
  {
    String sql = "select count(*) from SmsMo GHZQ_smsMO_Table";
    return searchRecByFilterCount(filter,sql);
  }

}
