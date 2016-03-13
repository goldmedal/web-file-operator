package mmdb.web.io;

import org.apache.hadoop.fs.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.httpclient.URI;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.IOUtils;

public class HdfsOperator {
	
	private static FileSystem hdfs;
	private String host = "hdfs://node1:8020";
	Configuration conf = new Configuration();

	public HdfsOperator() {
	
//		conf.addResource("/etc/hadoop/conf/hdfs-site.xml");
//		conf.addResource("/etc/hadoop/conf/core-site.xml");
//		conf.addResource("/etc/hadoop/conf/mapred-site.xml");
//		conf.addResource("/etc/hadoop/conf/yarn-site.xml");
		try {
			hdfs = FileSystem.get(new Path(host).toUri(),conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getIt() throws FileNotFoundException, IOException{
		
		Path full = new Path(this.host+"/user/root/test");
		//return hdfs.listFiles(full, false).toString();
		return hdfs.isFile(full)?"YES":"NO";		
	}
	
	public void getOutputStream(String path, InputStream is) {
		
		 Path fullPath = new Path(path);
		 FSDataOutputStream writer = null;
		 
		 try {
			 
		 	writer = hdfs.create(fullPath);
		 	IOUtils.copyBytes(is, writer, conf, true);
		 	
		 } catch (IOException e) {
			 
			// TODO Auto-generated catch block
			 e.printStackTrace();
		 }
		return;	
		
	}

}
