package com.kelefa.sms.dao;

import java.util.List;

public interface AbstractDAO
{
  public void deleteRec(int id) throws Exception;
  public void deleteRec(long id) throws Exception;

  public List selectAllRec(int pageno, int pagelist) throws Exception;

  public int selectAllRecCount() throws Exception;

  public int searchRecByFilterCount(String filter) throws Exception;

  public List searchRecByFilter(String filter, int pageno, int pagelist)
      throws Exception;

  public void updateRec(Object newOne) throws Exception;

  public void insertRec(Object newOne) throws Exception;

  public Object selectRecByPrimaryKey(int id) throws Exception;
  public Object selectRecByPrimaryKey(long id) throws Exception;
}
