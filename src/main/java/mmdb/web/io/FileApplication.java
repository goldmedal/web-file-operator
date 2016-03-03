package mmdb.web.io;


import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/")
public class FileApplication extends ResourceConfig {
    public FileApplication() {
        super(FileResource.class);
    }
}