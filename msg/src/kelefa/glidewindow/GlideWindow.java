package com.kelefa.glidewindow;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 滑动窗口.
 * 有些工作(例如网络通信)需要回应,但不需要一个一个的等待回应,可并发的进行,但需要控制流量,
 * 滑动窗口是比较好的方法.
 *
 * cmpp消息采用并发方式发送，加以滑动窗口流量控制，窗口大小参数W可配置:setSize( int size )，
 * 现阶段建议为16，即接收方在应答前一次收到的消息最多不超过16条。
 *
 *
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: 天颐通信</p>
 * @author 杨杰荣
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

	try {// 休息一下,让addJob()和remove()也有机会工作,
	     // 否则它们要等到( wait.size() >= size || todo.size()==0 )
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
    glideWindow.setSize( 10 ); // 窗口大小

    // 以下10个任务都被执行,都是没有回应
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

    // 窗口已满,"waiting"这个任务不会执行,处于等待状态
    glideWindow.addJob(
	"waiting",
	new Runnable()
	{
	  public void run()
	  {
	    System.out.println( "task running" );
	  }
	} );

    // 对0任务作出回应,"waiting"这个任务可以执行了
    glideWindow.remove( new Integer( 0 ) );
  }
}
