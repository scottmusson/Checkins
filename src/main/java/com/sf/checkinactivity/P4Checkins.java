package com.sf.checkinactivity;

import com.perforce.p4java.core.IChangelist;
import com.perforce.p4java.core.IChangelistSummary;
import com.perforce.p4java.core.file.FileSpecBuilder;
import com.perforce.p4java.core.file.IFileSpec;
import com.perforce.p4java.exception.*;
import com.perforce.p4java.server.IServer;
import com.perforce.p4java.server.ServerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

public class P4Checkins {
    private IServer server;
    public static final String PROPERTIES_FILE = "/com/sf/checkinactivity/repo.properties";

    public P4Checkins() {
    }

    public void connect()
            throws AccessException, ConfigException, RequestException, ConnectionException, NoSuchObjectException, ResourceException, URISyntaxException, IOException {
        InputStream is = null;
        try {
            is = this.getClass().getResourceAsStream(PROPERTIES_FILE);
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                String sName = props.getProperty("servername");
                server = ServerFactory.getServer(sName, props);
                server.connect();
            } else {
                throw new FileNotFoundException("Properties file was not found: " + PROPERTIES_FILE);
            }

        } finally {
            if (is != null) { try {is.close();} catch (Exception ignore) {} }
        }
    }

    public List<IChangelistSummary> getChangelistsForUser(String userid)
            throws AccessException, RequestException, ConnectionException {
        List<IFileSpec> fileSpecs = FileSpecBuilder.makeFileSpecList(new String[]{"//app/..."});

        return server.getChangelists(-1, fileSpecs, null, userid, false, true, false, false);
    }

    public IChangelist describeChangelist(IChangelistSummary iChangelistSummary)
            throws AccessException, RequestException, ConnectionException {
        return server.getChangelist(iChangelistSummary.getId());
    }
}