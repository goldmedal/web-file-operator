package mmdb.web.io;

import java.io.*;
import java.util.Enumeration;

import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FileUtils;

@Path("/operator")
public class FileResource {
    
	private String _baseDir = "C:/Users/Jax/Desktop";
	private String _bufferDir = "C:/Users/Jax/Desktop/tmp";
	
    @GET 
    @Path("getIt")
    @Produces("text/plain")
    public String getIt(String fileName) throws IOException {
    	
		HdfsOperator hdfs = new HdfsOperator();

        return hdfs.getIt();
    }
    
    /**
     * Response the specific file
     * @param fileName 
     * @return file content
     * @throws IOException
     */
    @GET 
    @Path("retrieve/{fileName}")
    
    public Response retrieve(@PathParam("fileName") String fileName) throws IOException { 
        File mergedPath = new File(this._baseDir, fileName);
        return Response.ok(mergedPath).build();
    }
    
    /**
     * delete the specific file
     * @param fileName
     * @return file deleting status
     * @return IOException
     * 
     */
    
    @DELETE
    @Path("delete/{fileName}")
    public Response delete(@PathParam("fileName") String fileName) throws IOException {
        File mergedPath = new File(this._baseDir, fileName);
        FileUtils.deleteQuietly(mergedPath);
        return Response.ok("OK").build();
    }

    /**
     * upload the specific file
     * @param fileName 
     * @param is
     * @return file uploading status
     * @throws IOException
     */
    @PUT
    @Path("store/{fileName}")
    @Consumes(MediaType.MULTIPART_FORM_DATA) 
    public Response store(@PathParam("fileName") String fileName, 
    		@FormDataParam("file") InputStream is) throws IOException{
        
        OutputStream os = null;
        try {
            
            File mergedPath = new File(this._baseDir, fileName);
            os = new FileOutputStream(mergedPath);
            IOUtils.copy(is, os);
        
        } catch (IOException ex) {
            
            ex.printStackTrace();
            Response.status(500).build();
            
        } finally {
            
            if (is != null){
                try {
                    is.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            
            if (os != null) {
                try {
                    os.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            
        }
        
        return Response.ok("OK").build();
        
    }
    
    /**
     * Uncompressing the uploading file and store
     * 
     * @param fileName
     * @param is
     * @return store and uncompress status
     * @throws IOException
     */
    
    @PUT
    @Path("uncompressAndStore/{fileName}")
    @Consumes(MediaType.MULTIPART_FORM_DATA) 
    public Response compressAndStore(@PathParam("fileName") String fileName, 
    		@FormDataParam("file") InputStream is) throws IOException{
        
        OutputStream os = null;
        try {
            
            File mergedPath = new File(this._bufferDir, fileName);
            os = new FileOutputStream(mergedPath);
            IOUtils.copy(is, os);
        
        } catch (IOException ex) {
            
            ex.printStackTrace();
            Response.status(500).build();
            
        } finally {
            
            if (is != null){
                try {
                    is.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            
            if (os != null) {
                try {
                    os.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            
        }
        
        // copress
        
        try {
        	
        	String outputPath = this._baseDir;
        	String zipFile = this._bufferDir+"/"+fileName;
        	this.uncompress(zipFile, outputPath);
        	
        } catch (IOException e) {
        	
        	e.printStackTrace();
        	
        }
        
        return Response.ok("OK").build();
        
    }    
    
    /**
     * Uncompress the archive zip file
     * @param archiveFile path of zip file
     * @param outputPath the output file
     * @throws IOException
     */
    
    private void uncompress(String archiveFile, String outputPath) throws IOException {
    
    	File outPath = new File(outputPath);
    	ZipFile zipFile = new ZipFile(new File(archiveFile));
    
    	Enumeration e = zipFile.getEntries();
    	while(e.hasMoreElements()){
    		
    		ZipArchiveEntry entry = (ZipArchiveEntry) e.nextElement();
    		File file = new File(outPath, entry.getName());
    		if(entry.isDirectory()){
    			
    			FileUtils.forceMkdir(file);;
    			
    		}else {
    			
    			InputStream is = zipFile.getInputStream(entry);
    			FileOutputStream os = FileUtils.openOutputStream(file);
    			try {
    				
    				IOUtils.copy(is, os);
    				
    			} finally {
    				
    				os.close();
    				is.close();
    				
    			}
    			file.setLastModified(entry.getTime());
    		}
    		
    	}
    	
    }

    @PUT
    @Path("storeToHDFS/{fileName}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    
	public Response storeToHDFS(@PathParam("fileName") String fileName, 
			@FormDataParam("file") InputStream is) throws IOException{
	    
		HdfsOperator hdfs = new HdfsOperator();
	    
	    try {
 
	    	hdfs.getOutputStream("/user/root/"+fileName, is);
	    
	    } finally {
	        
	        if (is != null){
	            try {
	                is.close();
	            } catch(IOException e) {
	                e.printStackTrace();
	            }
	        } 
	    }
	    
	    return Response.ok("OK").build();
	    
	}    
}
