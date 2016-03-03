package mmdb.web.io;

import java.io.*;
import org.apache.commons.io.*;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/operator")
public class FileResource {
    
	private String _baseDir = "C:/Users/Jax/Desktop";
	
    @GET 
    @Path("getIt")
    @Produces("text/plain")
    public String getIt(String fileName) {

        return "Hi there!"+fileName;
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
}
