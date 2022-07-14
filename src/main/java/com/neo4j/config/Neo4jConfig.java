package com.neo4j.config;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class Neo4jConfig {

    @Value(value = "${neo4j.db_neo4j}")
    private String dbNoe4j;

    @Value(value = "${neo4j.neo4j_url}")
    private String noe4jUrl;

    @Value(value = "${neo4j.username}")
    private String username;

    @Value(value = "${neo4j.password}")
    private String password;

    //使用内嵌式数据库
    @Bean
    public GraphDatabaseService getGraphDatabaseService() {
        GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
        GraphDatabaseService db = dbFactory.newEmbeddedDatabase(new File(dbNoe4j));
        return db;
    }

    //使用服务器式数据库
    @Bean
    public Driver getDrive() {
        return GraphDatabase.driver(noe4jUrl, AuthTokens.basic(username, password));
    }
}
