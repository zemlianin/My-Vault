package org.example;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app-settings")
public class AppSettings {
    private int port;

    private String dbdriver;

    private String dbhost;

    private String dbport;

    private String dbname;

    private String dbusername;

    private String dbpassword;

    public void setPort(int port) {
        this.port = port;
    }

    public void setDbdriver(String dbdriver) {
        this.dbdriver = dbdriver;
    }

    public void setDbhost(String dbhost) {
        this.dbhost = dbhost;
    }

    public void setDbport(String dbport) {
        this.dbport = dbport;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public void setDbusername(String dbusername) {
        this.dbusername = dbusername;
    }

    public void setDbpassword(String dbpassword) {
        this.dbpassword = dbpassword;
    }

    public int getPort() {
        return port;
    }

    public String getDbdriver() {
        return dbdriver;
    }

    public String getDbhost() {
        return dbhost;
    }

    public String getDbport() {
        return dbport;
    }

    public String getDbname() {
        return dbname;
    }

    public String getDbusername() {
        return dbusername;
    }

    public String getDbpassword() {
        return dbpassword;
    }
}

