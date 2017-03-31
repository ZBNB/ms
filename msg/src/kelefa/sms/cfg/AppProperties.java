package com.kelefa.sms.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AppProperties
{
  private static Log log = LogFactory.getLog( AppProperties.class );


  public AppProperties()
  {
  }

  private static final Properties conf_PROPERTIES;
  static
  {
    conf_PROPERTIES = new Properties();

    InputStream stream = AppProperties.class.getResourceAsStream(
	"/application.properties" );
    if ( stream == null ) {
      log.warn( "application.properties not found" );
    }
    else {
      try {
	conf_PROPERTIES.load( stream );
	log.info( "loaded properties from resource application.properties: " +
		  conf_PROPERTIES );
      }
      catch ( Exception e ) {
	log.error( "problem loading properties from application.properties" );
      }
      finally {
	try {
	  stream.close();
	}
	catch ( IOException ioe ) {
	  log.error( "could not close stream on application.properties", ioe );
	}
      }
    }
  }

  public static String get( String key )
  {
    return conf_PROPERTIES.getProperty( key );
  }

  public static int getIntValue( String key )
  {
    return getIntValue( key, 0 );
  }

  public static int getIntValue( String key, int defaultValue )
  {
    String str = conf_PROPERTIES.getProperty( key );
    int value = 0;
    try {
      value = Integer.parseInt( str );
    }
    catch ( NumberFormatException ex ) {
      value = defaultValue;
    }
    return value;
  }
}
