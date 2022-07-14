package com.neo4j.controller;

import com.neo4j.entity.Code;
import com.neo4j.entity.MyLabel;
import com.neo4j.entity.MyRelationshipType;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.graphdb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * @author chengkun
 * @version v1.0
 * @create 2021/3/29 15:32
 **/
@Api(tags = "neo4j增删改查")
@RestController
@Log4j2
@RequestMapping("/neo4j")
public class Neo4jController {

    @Autowired
    private GraphDatabaseService graphDB; //使用内嵌式数据库

    @Autowired
    private Driver driver; //使用服务器式数据库(推荐)

    @ApiOperation(value = "创建数据（使用内嵌式数据库）")
    @GetMapping(value = "/create", produces = "application/text;charset=UTF-8")
    public String create() {
        Transaction tx = graphDB.beginTx();
        try {
            Node java = graphDB.createNode(MyLabel.JAVA);
            java.setProperty("id", 1);
            java.setProperty("name", "java");
            Node c = graphDB.createNode(MyLabel.C);
            c.setProperty("id", 2);
            c.setProperty("name", "c");
            Node python = graphDB.createNode(MyLabel.PYTHON);
            python.setProperty("id", 3);
            python.setProperty("name", "python");
            Node scala = graphDB.createNode(MyLabel.SCALA);
            scala.setProperty("id", 4);
            scala.setProperty("name", "scala");
            Node mysql = graphDB.createNode(MyLabel.MYSQL);
            mysql.setProperty("id", 5);
            mysql.setProperty("name", "mysql");
            Node neo4j = graphDB.createNode(MyLabel.NEO4J);
            neo4j.setProperty("id", 6);
            neo4j.setProperty("name", "neo4j");
            Relationship javaMysql = java.createRelationshipTo(mysql, MyRelationshipType.JVM_LANGIAGES);
            javaMysql.setProperty("id", 1);
            javaMysql.setProperty("name", "java连接mysql");
            Relationship cMysql = c.createRelationshipTo(mysql, MyRelationshipType.NON_JVM_LANGIAGES);
            cMysql.setProperty("id", 2);
            cMysql.setProperty("name", "c连接mysql");
            Relationship pythonMysql = python.createRelationshipTo(mysql, MyRelationshipType.NON_JVM_LANGIAGES);
            pythonMysql.setProperty("id", 3);
            pythonMysql.setProperty("name", "python连接mysql");
            Relationship javaNeo4j = java.createRelationshipTo(neo4j, MyRelationshipType.JVM_LANGIAGES);
            javaNeo4j.setProperty("id", 4);
            javaNeo4j.setProperty("name", "java连接neo4j");
            Relationship scalaNeo4j = scala.createRelationshipTo(neo4j, MyRelationshipType.NON_JVM_LANGIAGES);
            scalaNeo4j.setProperty("id", 5);
            scalaNeo4j.setProperty("name", "scala连接neo4j");
            tx.success();
        } finally {
            tx.close();
        }
        return "创建成功";
    }

    @ApiOperation(value = "根据id查询节点（使用内嵌式数据库）")
    @GetMapping(value = "/getNode", produces = "application/text;charset=UTF-8")
    public String getNode(@RequestParam("id") Integer id) {
        Transaction tx = graphDB.beginTx();
        Node node;
        try {
            node = graphDB.findNode(MyLabel.JAVA, "id", id); //这里类型必须与保存的类型一致
            System.out.println(node.getId());
            System.out.println(node.getLabels());
            System.out.println(node.getAllProperties());
            tx.success();
        } finally {
            tx.close();
        }
        return "查询成功";
    }

    @ApiOperation(value = "查看所有（使用内嵌式数据库）")
    @GetMapping(value = "/findAll", produces = "application/text;charset=UTF-8")
    public String findAll() {
        Transaction tx = graphDB.beginTx();
        try {
            for (Node node : graphDB.getAllNodes()) {
                System.out.println(node.getId());
                System.out.println(node.getLabels());
                System.out.println(node.getAllProperties());
                for (Relationship relationship : node.getRelationships()) {
                    System.out.println(relationship.getId());
                    System.out.println(relationship.getType());
                    System.out.println(relationship.getAllProperties());
                }
            }
            tx.success();
        } finally {
            tx.close();
        }
        return "查询成功";
    }


    @ApiOperation(value = "删除节点以及关联的边（使用内嵌式数据库）")
    @GetMapping(value = "/deleteNode", produces = "application/text;charset=UTF-8")
    public String deleteNode(@RequestParam("id") Integer id) {
        Transaction tx = graphDB.beginTx();
        try {
            Node scala = graphDB.findNode(MyLabel.SCALA, "id", id);
            Iterable<Relationship> relationships = scala.getRelationships(MyRelationshipType.NON_JVM_LANGIAGES);
            //删除边
            for (Relationship relationship : relationships) {
                relationship.delete();
            }
            //删除节点
            scala.delete();
            System.out.println("delete ok");
            tx.success();
        } finally {
            tx.close();
        }
        return "删除成功";
    }

    @ApiOperation(value = "修改节点以及关联的边（使用内嵌式数据库）")
    @GetMapping(value = "/updateNode", produces = "application/text;charset=UTF-8")
    public String updateNode(@RequestParam("id") Integer id) {
        Transaction tx = graphDB.beginTx();
        try {
            Node c = graphDB.findNode(MyLabel.C, "id", id);
            Iterable<Relationship> relationships = c.getRelationships(Direction.OUTGOING, MyRelationshipType.NON_JVM_LANGIAGES);
            //修改边
            for (Relationship relationship : relationships) {
                relationship.setProperty("type", "c->out->other");
            }
            //修改节点
            c.setProperty("type", "c语言");
            System.out.println("update ok");
            tx.success();
        } finally {
            tx.close();
        }
        return "修改成功";
    }

    @ApiOperation(value = "删除所有（使用内嵌式数据库）")
    @GetMapping(value = "/deleteAll", produces = "application/text;charset=UTF-8")
    public String deleteAll() {
        Transaction tx = graphDB.beginTx();
        try {
            for (Node node : graphDB.getAllNodes()) {
                for (Relationship relationship : node.getRelationships()) {
                    relationship.delete();
                }
                node.delete();
            }
            tx.success();
        } finally {
            tx.close();
        }
        return "删除所有";
    }

    @ApiOperation(value = "创建数据（使用服务器式数据库）")
    @GetMapping(value = "create1", produces = "application/text;charset=UTF-8")
    public String create1() {

        try {
            Session session = driver.session();

            session.run("CREATE (a:Person {id: {id}, name: {name}, title: {title}})",
                    parameters("id", 1, "name", "Arthur001", "title", "King001"));

            StatementResult result = session.run("MATCH (a:Person) WHERE a.name = {name} RETURN a.id as id,a.name AS name, a.title AS title",
                    parameters("name", "Arthur001"));

            while (result.hasNext()) {
                Record record = result.next();
                System.out.println(record.get("id").asInt() + " " + record.get("title").asString() + " " + record.get("name").asString());
            }

            session.close();
            System.out.println("使用服务器式数据库创建数据成功");
        } catch (Exception e) {
            log.info("使用服务器式数据库创建数据失败");
            return "使用服务器式数据库创建数据失败";
        }
        return "使用服务器式数据库创建数据成功";
    }


    @ApiOperation(value = "修改数据（使用服务器式数据库）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "修改节点id"),
            @ApiImplicitParam(name = "name", value = "修改后的名称"),
    })
    @GetMapping(value = "update", produces = "application/json;charset=UTF-8")
    public void update(@RequestParam("id") Integer id, @RequestParam("name") String name) {
        try {
            Session session = driver.session();
            StatementResult result = session.run("MATCH (a:Person) WHERE a.id={id}  SET a.name = {name} RETURN a.id as id,a.name AS name, a.title AS title",
                    parameters("id", id, "name", name));

            while (result.hasNext()) {
                Record record = result.next();
                System.out.println(record.get("id").asInt() + " " + record.get("title").asString() + " " + record.get("name").asString());
            }

            session.close();
            System.out.println("使用服务器式数据库修改数据成功");
        } catch (Exception e) {
            log.info("使用服务器式数据库修改数据失败");
        }
    }

    @ApiOperation(value = "添加关系（使用服务器式数据库）")
    @PostMapping(value = "relate", produces = "application/json;charset=UTF-8")
    public void relate(@RequestBody Code code) {

        try {
            Session session = driver.session();

            session.run("MATCH (a:" + code.getNodeFromLabel() + "), (b:" + code.getNodeToLabel() + ") " +
                    "WHERE a.id = " + code.getNodeFromId() + " AND b.id = " + code.getNodeToId()
                    + " CREATE (a)-[:" + code.getRelation() + "]->(b)");

            session.close();
            System.out.println("使用服务器式数据库添加边成功");
        } catch (Exception e) {
            log.info("使用服务器式数据库添加边失败");
        }
    }

    @ApiOperation(value = "删除数据（使用服务器式数据库）")
    @GetMapping(value = "delete", produces = "application/text;charset=UTF-8")
    public String delete(@RequestParam("id") Integer id) {
        try {
            Session session = driver.session();
            session.run("match (n:Person) where n.id = {id} delete n",
                    parameters("id", id));

            session.close();
            System.out.println("使用服务器式数据库删除数据成功");
        } catch (Exception e) {
            log.info("使用服务器式数据库删除数据失败");
            return "使用服务器式数据库删除数据失败";
        }
        return "使用服务器式数据库删除数据成功";
    }

    @ApiOperation(value = "删除关系（使用服务器式数据库）")
    @PostMapping(value = "deleteRelate", produces = "application/json;charset=UTF-8")
    public void deleteRelate(@RequestBody Code code) {
        try {
            Session session = driver.session();
            //不知道为什么执行报错
//            session.run("MATCH (a:{NodeFromLabel})-[r:{relation}]->(b:{nodeToLabel}) WHERE a.id={nodeFromId} and b.id={nodeToId} DELETE r",
//                    parameters("NodeFromLabel", code.getNodeFromLabel(), "relation", code.getRelation(), "nodeToLabel", code.getNodeToLabel(), "nodeFromId", code.getNodeFromId(), "nodeToId", code.getNodeToId()));
            session.run("MATCH (a:" + code.getNodeFromLabel() + ")-[r:" + code.getRelation() + "]->(b:" + code.getNodeToLabel() + ") WHERE a.id = " + code.getNodeFromId() + " and b.id = " + code.getNodeToId() + " DELETE r");

            session.close();
            System.out.println("使用服务器式数据库删除数据成功");
        } catch (Exception e) {
            log.info("使用服务器式数据库删除数据失败");
        }
    }

    @ApiOperation(value = "查询数据（使用服务器式数据库）")
    @GetMapping(value = "search", produces = "application/json;charset=UTF-8")
    public List<String> search() {
        List<String> resultList = new ArrayList<>();
        try {
            Session session = driver.session();
            StatementResult result = session.run("match (n) return n.id as id,n.name as name");

            while (result.hasNext()) {
                Record record = result.next();
                resultList.add(record.get("id").toString() + " " + record.get("name").toString());
            }
            session.close();
            System.out.println("使用服务器式数据库查询数据成功");
        } catch (Exception e) {
            log.info("使用服务器式数据库查询数据失败");
        }
        return resultList;
    }


}
