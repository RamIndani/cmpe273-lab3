package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.common.hash.*;
import com.google.common.hash.Hashing;
/**
 * Distributed cache service
 * 
 */
public class DistributedCacheService implements CacheServiceInterface {
    private final int numberOfReplicas;
    private final ArrayList<String> servers;
    private final HashMap<Integer, String> circle = new HashMap<Integer, String>();
    public DistributedCacheService(ArrayList<String> nodes)  throws NoSuchAlgorithmException{
        this.servers = nodes;
    	this.numberOfReplicas = nodes.size();
    }

    private String serverURL(long key) {
    	return servers.get(Hashing.consistentHash(Hashing.md5().hashString(Long.toString(key)), numberOfReplicas));
    }
    /**
     * @see edu.sjsu.cmpe.cache.client.CacheServiceInterface#get(long)
     */
    @Override
    public String get(long key) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get((serverURL(key)) + "/cache/{key}")
                    .header("accept", "application/json")
                    .routeParam("key", Long.toString(key)).asJson();
        } catch (UnirestException e) {
            System.err.println(e);
        }
        String value = response.getBody().getObject().getString("value");

        return value;
    }

    /**
     * @see edu.sjsu.cmpe.cache.client.CacheServiceInterface#put(long,
     *      java.lang.String)
     */
    @Override
    public void put(long key, String value) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest
                    .put((serverURL(key)) + "/cache/{key}/{value}")
                    .header("accept", "application/json")
                    .routeParam("key", Long.toString(key))
                    .routeParam("value", value).asJson();
        } catch (UnirestException e) {
            System.err.println(e);
        }

        if (response.getCode() != 200) {
            System.out.println("Failed to add to the cache.");
        }
    }
}
