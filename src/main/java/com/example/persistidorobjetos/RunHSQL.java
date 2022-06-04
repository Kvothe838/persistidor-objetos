package com.example.persistidorobjetos;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;

public class RunHSQL
{
	public static void main(String[] args)
	{
		try
		{
			 String[] propsDB = {"-database.0"
			 ,"file:database/persistidor"
			 ,"-dbname.0"
			 ,"xdb"};
							
			 HsqlProperties props = HsqlProperties.argArrayToProps(propsDB,
			 "server");
			 Server dbserver= new Server();
			 dbserver.setProperties(props);
			 dbserver.start();
			 Thread.sleep(2000);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
