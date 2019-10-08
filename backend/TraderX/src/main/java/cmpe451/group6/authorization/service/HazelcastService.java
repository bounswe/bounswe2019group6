package cmpe451.group6.authorization.service;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.stereotype.Component;

@Component
public class HazelcastService {

    //@Value("${security.jwt.token.expire-length}")
    private final static int TOKEN_EXPIRATION_TIME_MS = 1800000;

    private static final String HAZELCAST_BLACKLIST_MAP_NAME = "blacklist_map";

    private static HazelcastInstance hazelcastInstance = initHazelcast();

    private static HazelcastInstance initHazelcast(){
        Config config = new Config();
        config.getGroupConfig().setName("BLACKLIST_CLUSTER");
        config.getMapConfig(HAZELCAST_BLACKLIST_MAP_NAME).setTimeToLiveSeconds(TOKEN_EXPIRATION_TIME_MS);
        return Hazelcast.newHazelcastInstance(config);
    }

    private static IMap<String,String> getBlacklistMap() {
        return hazelcastInstance.getMap(HAZELCAST_BLACKLIST_MAP_NAME);
    }

    public static void putBlacklist(String token, String username){
        getBlacklistMap().put(token, username);
    }

    public static boolean isInBlacklist(String token){
        return getBlacklistMap().get(token) != null;
    }
}
