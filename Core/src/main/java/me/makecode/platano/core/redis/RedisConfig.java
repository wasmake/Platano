package me.makecode.platano.core.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Andrew R.
 */
@Getter
@AllArgsConstructor
public class RedisConfig {

    private final String prefix;
    private final String host;
    private final String password;
    private final int port;
    private final int timeout;
    private final int maxIdle;
    private final int maxTotal;

}