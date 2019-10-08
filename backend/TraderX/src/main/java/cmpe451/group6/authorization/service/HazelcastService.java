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
    private static final String HAZELCAST_WHITELIST_MAP_NAME = "whitelist_map";

    private static HazelcastInstance hazelcastInstance = initHazelcast();

    private static HazelcastInstance initHazelcast(){
        Config config = new Config();
        config.getGroupConfig().setName("BLACKLIST_CLUSTER");

        config.getMapConfig(HAZELCAST_BLACKLIST_MAP_NAME).setTimeToLiveSeconds(TOKEN_EXPIRATION_TIME_MS);
        config.getMapConfig(HAZELCAST_BLACKLIST_MAP_NAME).setTimeToLiveSeconds(TOKEN_EXPIRATION_TIME_MS);

        return Hazelcast.newHazelcastInstance(config);
    }

    private static IMap<String,String> getBlacklistMap() {
        return hazelcastInstance.getMap(HAZELCAST_BLACKLIST_MAP_NAME);
    }

    private static IMap<String,String> getWhitelistMap() {
        return hazelcastInstance.getMap(HAZELCAST_WHITELIST_MAP_NAME);
    }

    private static void removeWhiteToken(String token){
        getWhitelistMap().remove(token);
    }

    public static void invalidateToken(String token, String username){
        removeWhiteToken(token);
        getBlacklistMap().put(token, username);
    }

    public static void putWhiteToken(String token, String username){
        getWhitelistMap().put(token, username);
    }

    public static boolean isWhiteToken(String username){
        return getWhitelistMap().containsValue(username);
    }

    public static boolean isBlackToken(String token){
        return getBlacklistMap().get(token) != null;
    }

}
