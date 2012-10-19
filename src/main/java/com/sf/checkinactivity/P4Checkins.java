package com.sf.checkinactivity;

import com.perforce.p4java.core.IChangelist;
import com.perforce.p4java.core.IChangelistSummary;
import com.perforce.p4java.core.file.FileSpecBuilder;
import com.perforce.p4java.core.file.IFileSpec;
import com.perforce.p4java.exception.*;
import com.perforce.p4java.server.IServer;
import com.perforce.p4java.server.ServerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

public class P4Checkins {
    private IServer server;

    public P4Checkins() {
    }

    public void connect(String serverName, String userName, String password, String clientName)
            throws AccessException, ConfigException, RequestException, ConnectionException, NoSuchObjectException, ResourceException, URISyntaxException, IOException {
        Properties props = new Properties();
        if (serverName != null ) {
            props.setProperty("userName", userName);
            props.setProperty("password", password);
            props.setProperty("clientname", clientName);
        }
        InputStream is = null;
        try {
            is = this.getClass().getResourceAsStream("/com/sf/checkinactivity/repo.properties");
            if (is != null) {
                props.load(is);
            }
        } finally {
            if (is != null) { try {is.close();} catch (Exception ignore) {} }
        }

        String sName = props.getProperty("servername", serverName);
        server = ServerFactory.getServer(sName, props);
        server.connect();
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