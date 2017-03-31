package com.kelefa.sms.job;

import java.text.ParseException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import com.kelefa.cmpp.mo.MoManager;
import com.kelefa.sms.cfg.AppProperties;
import com.kelefa.sms.util.ComponentUtil;

public class QuartzInit
    extends HttpServlet
{
  transient Scheduler sched;

  public QuartzInit()
  {
    SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
    try {
      sched = schedFact.getScheduler();
    }
    catch ( SchedulerException ex ) {
    }
  }

  public void init( ServletConfig servletConfig )
      throws ServletException
  {
    ComponentUtil.application = servletConfig.getServletContext();

    MoManager manager = MoManager.getInstance();
    startQuartz();
  }

  public void destroy()
  {
    try {
      sched.resumeAll();
      sched.shutdown( true );
    }
    catch ( SchedulerException ex ) {
      ex.printStackTrace();
    }
  }

  public void startQuartz()
  {
    try {
      JobDetail jobDetail = new JobDetail( "myJob",
					   sched.DEFAULT_GROUP,
					   SmsSender.class );
      String cronExpression = AppProperties.get( "cronExpression" );
      CronTrigger trigger = new CronTrigger( "trigg1", "group1", "myJob",
					     sched.DEFAULT_GROUP,
					     cronExpression );

      sched.scheduleJob( jobDetail, trigger );

      sched.start();
    }
    catch ( ParseException ex ) {
      ex.printStackTrace();
    }
    catch ( SchedulerException ex ) {
      ex.printStackTrace();
    }
  }
}
