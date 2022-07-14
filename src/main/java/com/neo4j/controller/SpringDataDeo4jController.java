package com.neo4j.controller;

import com.neo4j.entity.FriendshipRelation;
import com.neo4j.entity.Student;
import com.neo4j.repository.StudentRepository;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "spring-data-neo4j增删改查")
@RestController
@Log4j2
@RequestMapping("/spring-data-neo4j")
public class SpringDataDeo4jController {

    @Autowired
    private StudentRepository studentRepository;

    @ApiOperation(value = " 保存单个节点（使用spring-data-neo4j）")
    @PostMapping(value = "/saveStudentNode", produces = "application/text;charset=UTF-8")
    public String saveStudentNode(String name, int age) {
        Student student = new Student();
        student.setName(name);
        student.setAge(age);
        studentRepository.save(student);
        return "创建成功";
    }

    @ApiOperation(value = "保存批量节点（使用spring-data-neo4j）")
    @GetMapping(value = "saveAllStudentNode", produces = "application/text;charset=UTF-8")
    public String saveAllStudentNode() {
        Student student1 = new Student();
        student1.setName("张三");
        student1.setAge(18);
        Student student2 = new Student();
        student2.setName("李四");
        student2.setAge(19);
        Student student3 = new Student();
        student3.setName("王五");
        student3.setAge(18);
        Student student4 = new Student();
        student4.setName("赵六");
        student4.setAge(20);
        List<Student> list = Lists.newArrayList(student1, student2, student3, student4);
        studentRepository.saveAll(list);
        return "创建成功";
    }

    @ApiOperation(value = "根据name查询学生（使用spring-data-neo4j）")
    @PostMapping(value = "/findStudentByName", produces = "application/json;charset=UTF-8")
    @ApiImplicitParam(name = "name", value = "学生名称")
    public Student findStudentByName(@RequestParam("name") String name) {
        Student student = studentRepository.findByName(name);
        return student;
    }

    @ApiOperation(value = "修改学生名称（使用spring-data-neo4j）")
    @GetMapping(value = "/updateStudentByName", produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "修改前学生名称"),
            @ApiImplicitParam(name = "updateName", value = "修改后学生名称"),
    })
    public Student updateStudentByName(String name, String updateName) {
        Student student = studentRepository.findByName(name);
        student.setName(updateName);
        studentRepository.save(student);
        return student;
    }

    @ApiOperation(value = "根据名称删除学生（使用spring-data-neo4j）")
    @GetMapping(value = "/deleteStudentByName", produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "修改前学生名称"),
    })
    public Student deleteStudentByName(String name) {
        Student student = studentRepository.findByName(name);
        studentRepository.delete(student);
        return student;
    }

    @ApiOperation(value = "删除所有学生（使用spring-data-neo4j）")
    @GetMapping(value = "/deleteAllStudent", produces = "application/text;charset=UTF-8")
    public String deleteAllStudent() {
        studentRepository.deleteAll();
        return "删除成功";
    }

    @ApiOperation(value = "查询全部（使用spring-data-neo4j）")
    @GetMapping(value = "/findAll", produces = "application/json;charset=UTF-8")
    public List<Student> findAll() {
        List<Student> studentNodeList = Lists.newArrayList(studentRepository.findAll());
        return studentNodeList;
    }

    /**
     * 分页查询
     */
    @ApiOperation(value = "分页查询（使用spring-data-neo4j）")
    @GetMapping(value = "/pageFindAll", produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页"),
            @ApiImplicitParam(name = "size", value = "每页条数"),
    })
    public Page<Student> pageFindAll(int page, int size) {
        // page从0开始
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Student> studentNodePage = studentRepository.findAll(pageable);
        return studentNodePage;
    }

    @ApiOperation(value = "保存友谊关系（使用spring-data-neo4j）")
    @GetMapping(value = "/saveFriendShip", produces = "application/text;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "studentFromName", value = "关系起点名称"),
            @ApiImplicitParam(name = "studentToName", value = "关系终点名称"),
    })
    public String saveFriendShip(String studentFromName, String studentToName) {
        Student studentFrom = studentRepository.findByName(studentFromName);
        Student studentTo = studentRepository.findByName(studentToName);
        FriendshipRelation studentFromToRelation = new FriendshipRelation();
        studentFromToRelation.setName("友谊关系");
        // 添加from
        studentFromToRelation.setFrom(studentFrom);
        //添加to
        studentFromToRelation.setTo(studentTo);
        //只需要在from节点保存关系即可
        studentFrom.addOutRelation(studentFromToRelation);
        studentRepository.save(studentFrom);
        return "保存成功";
    }


    @ApiOperation(value = "保存双向友谊关系（使用spring-data-neo4j）")
    @GetMapping(value = "/saveBrotherFriendShip", produces = "application/text;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name1", value = "学生名称1"),
            @ApiImplicitParam(name = "name2", value = "学生名称2"),
    })
    public String saveBrotherFriendShip(String name1, String name2) {
        Student studentFrom = studentRepository.findByName(name1);
        Student studentTo = studentRepository.findByName(name2);
        FriendshipRelation studentFromToRelation = new FriendshipRelation();
        studentFromToRelation.setName("友谊关系");
        // 添加from
        studentFromToRelation.setFrom(studentTo);
        //添加to
        studentFromToRelation.setTo(studentTo);
        //from->to
        studentFrom.addOutRelation(studentFromToRelation);

        FriendshipRelation studentToFromRelation = new FriendshipRelation();
        studentToFromRelation.setName("友谊关系");
        // 添加from
        studentToFromRelation.setFrom(studentTo);
        //添加to
        studentToFromRelation.setTo(studentFrom);
        //to->from
        studentFrom.addInRelation(studentToFromRelation);
        studentRepository.save(studentFrom);
        return "保存成功";
    }

    @ApiOperation(value = "根据名称查询in关系（使用spring-data-neo4j）")
    @GetMapping(value = "/inFriendshipByName", produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "学生名称"),
    })
    public List<FriendshipRelation> inFriendshipByName(@RequestParam("name") String name) {
        List<FriendshipRelation> friendshipRelations = studentRepository.inFriendship(name);
        return friendshipRelations;
    }

    @ApiOperation(value = "根据名称查询out关系（使用spring-data-neo4j）")
    @GetMapping(value = "/outFriendshipByName", produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "学生名称"),
    })
    public List<FriendshipRelation> outFriendshipByName(String name) {
        List<FriendshipRelation> friendshipRelations = studentRepository.outFriendship(name);
        return friendshipRelations;
    }

    @ApiOperation(value = "获取兄弟关系（使用spring-data-neo4j）")
    @GetMapping(value = "/getBrotherFriendShip", produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "学生名称"),
    })
    public List<FriendshipRelation> getBrotherFriendShip(String name) {
        List<FriendshipRelation> friendshipRelations = studentRepository.bothFriendship(name);
        return friendshipRelations;
    }
}
