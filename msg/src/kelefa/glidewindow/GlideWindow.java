package com.kelefa.glidewindow;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * ��������.
 * ��Щ����(��������ͨ��)��Ҫ��Ӧ,������Ҫһ��һ���ĵȴ���Ӧ,�ɲ����Ľ���,����Ҫ��������,
 * ���������ǱȽϺõķ���.
 *
 * cmpp��Ϣ���ò�����ʽ���ͣ����Ի��������������ƣ����ڴ�С����W������:setSize( int size )��
 * �ֽ׶ν���Ϊ16�������շ���Ӧ��ǰһ���յ�����Ϣ��಻����16����
 *
 *
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: ����ͨ��</p>
 * @author �����
 * @version 1.0
 */
public class GlideWindow extends Thread
{
  private static final Logger log = Logger.getLogger(GlideWindow.class);

  private int size = 10;
  private Map todo = new LinkedHashMap();
  private Map wait = new HashMap();

  public GlideWindow()
  {
    start();
//    try{
//      Thread.sleep( 100 );
//    }
//    catch ( InterruptedException ex ){
//      ex.printStackTrace();
//    }
  }

  public void run()
  {
    synchronized ( this )
    {
      for (;;)
      {
	while ( wait.size() >= size || todo.size()==0 )
	{
	  log.debug("todo size: "+todo.size());
	  log.debug("wait size: "+wait.size());
	  try {
	    this.wait();
	  }
	  catch ( InterruptedException ignored ) {
	    log.warn(ignored.getMessage());
	  }
	}

	Object key = ( Object ) todo.keySet().iterator().next();
	Runnable job = ( Runnable ) todo.remove( key );
	wait.put( key, job );
	log.debug( "put to wait " + key );
	job.run();

	try {// ��Ϣһ��,��addJob()��remove()Ҳ�л��Ṥ��,
	     // ��������Ҫ�ȵ�( wait.size() >= size || todo.size()==0 )
	  this.wait( 10 );
	}
	catch ( InterruptedException ex ) {
	}
      }
    }
  }


  public synchronized void addJob( Object key, Runnable job )
  {
    log.debug("add " + key);
    todo.put( key, job );
    notifyAll();
  }

  public synchronized Object remove( Object key )
  {
    log.debug("remove "+key);
    notifyAll();
    return wait.remove(key);
  }

  public void setSize( int size )
  {
    this.size = size;
  }

  public int getTodoSize(  )
  {
    return todo.size();
  }

  public int getWaitSize(  )
  {
    return wait.size();
  }

  public int getSize(  )
  {
    return size;
  }

  public static void main(String[] args)
  {
    GlideWindow glideWindow = new GlideWindow();
    glideWindow.setSize( 10 ); // ���ڴ�С

    // ����10�����񶼱�ִ��,����û�л�Ӧ
    synchronized (glideWindow){
    for ( int i = 0; i < 10; i++ )
    {
      glideWindow.addJob(
	  new Integer( i ),
	  new Runnable()
	  {
	    public void run()
	    {
	      System.out.println( "task running" );
	    }
	  } );
    }}

    // ��������,"waiting"������񲻻�ִ��,���ڵȴ�״̬
    glideWindow.addJob(
	"waiting",
	new Runnable()
	{
	  public void run()
	  {
	    System.out.println( "task running" );
	  }
	} );

    // ��0����������Ӧ,"waiting"����������ִ����
    glideWindow.remove( new Integer( 0 ) );
  }
}
