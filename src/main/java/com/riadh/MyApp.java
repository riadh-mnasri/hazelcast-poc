package com.riadh;

import com.riadh.config.CacheConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Queue;

@SpringBootApplication
public class MyApp
{
    public static void main( String[] args )    {

        ApplicationContext ctx = SpringApplication.run(MyApp.class, args);
        CacheConfig cacheConfig = ctx.getBean("cacheConfig", CacheConfig.class);
        HazelcastInstance hazelcastInstance = cacheConfig.hazelcastInstance();
        Map<Integer, String> customers = hazelcastInstance.getMap( "customers" );
        customers.put( 1, "Franck" );
        customers.put( 2, "Jean" );
        customers.put( 3, "Marcel" );

        System.out.println( "Customer with key 1: " + customers.get(1) );
        System.out.println( "Map Size:" + customers.size() );

        Queue<String> queueCustomers = hazelcastInstance.getQueue( "customers" );
        queueCustomers.offer( "Tom" );
        queueCustomers.offer( "Mary" );
        queueCustomers.offer( "Jane" );

        System.out.println( "First customer: " + queueCustomers.poll() );
        System.out.println( "Second customer: "+ queueCustomers.peek() );
        System.out.println( "Queue size: " + queueCustomers.size() );
    }
}
