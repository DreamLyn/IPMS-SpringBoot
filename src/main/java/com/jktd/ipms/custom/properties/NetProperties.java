package com.jktd.ipms.custom.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "net")
@PropertySource(value = "classpath:config/netconfig.properties")
@Component
public class NetProperties {

    private String syncport;
    private String beatport;

    public String getBeatport() {
        return beatport;
    }

    public void setBeatport(String beatport) {
        this.beatport = beatport;
    }

    public String getSyncport() {
        return syncport;
    }

    public void setSyncport(String syncport) {
        this.syncport = syncport;
    }
}
