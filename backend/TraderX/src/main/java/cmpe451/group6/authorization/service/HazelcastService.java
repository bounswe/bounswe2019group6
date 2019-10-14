package cmpe451.group6.authorization.service;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class HazelcastService {

    private final int TOKEN_EXPIRATION_TIME_MS;

    private final String HAZELCAST_BLACKLIST_MAP_NAME = "blacklist_map";
    private final String HAZELCAST_WHITELIST_MAP_NAME = "whitelist_map";

    private HazelcastInstance hazelcastInstance;

    @Autowired
    public HazelcastService(@Value("${security.jwt.token.expire-length}") String expireLength) {
        this.TOKEN_EXPIRATION_TIME_MS = Integer.parseInt(expireLength);

        Config config = new Config();
        config.getGroupConfig().setName("TOKEN-MAP-CLUSTER");

        config.getMapConfig(HAZELCAST_BLACKLIST_MAP_NAME).setTimeToLiveSeconds(TOKEN_EXPIRATION_TIME_MS / 1000);
        config.getMapConfig(HAZELCAST_BLACKLIST_MAP_NAME).setTimeToLiveSeconds(TOKEN_EXPIRATION_TIME_MS / 1000);

        hazelcastInstance = Hazelcast.newHazelcastInstance(config);

    }

    public int getTOKEN_EXPIRATION_TIME_MS() {
        return TOKEN_EXPIRATION_TIME_MS;
    }

    private IMap<String,String> getBlacklistMap() {
        return hazelcastInstance.getMap(HAZELCAST_BLACKLIST_MAP_NAME);
    }

    private IMap<String,String> getWhitelistMap() {
        return hazelcastInstance.getMap(HAZELCAST_WHITELIST_MAP_NAME);
    }

    private void removeWhiteToken(String token){
        getWhitelistMap().remove(token);
    }

    public void invalidateToken(String token, String username){
        removeWhiteToken(token);
        getBlacklistMap().put(token, username);
    }

    public void putWhiteToken(String token, String username){
        getWhitelistMap().put(token, username);
    }

    public boolean isWhiteToken(String username){
        return getWhitelistMap().containsValue(username);
    }

    public boolean isBlackToken(String token){
        return getBlacklistMap().get(token) != null;
    }

    public int whiteTokensCount(String username){
         return getWhitelistMap().values((Predicate) entry -> entry.getValue().equals(username)).size();
    }

}
