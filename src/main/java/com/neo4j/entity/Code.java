package com.neo4j.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "neo4j参数实体类")
public class Code {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "节点")
    private String node;
    @ApiModelProperty(value = "关系")
    private String relation;
    @ApiModelProperty(value = "属性")
    private String property;
    @ApiModelProperty(value = "节点标签")
    private String label;
    @ApiModelProperty(value = "边起点id")
    private Integer nodeFromId;
    @ApiModelProperty(value = "边起点标签")
    private String nodeFromLabel;
    @ApiModelProperty(value = "边终点id")
    private Integer nodeToId;
    @ApiModelProperty(value = "边终点标签")
    private String nodeToLabel;
    @ApiModelProperty(value = "条件语句")
    private String where;
    @ApiModelProperty(value = "修改语句")
    private String update;
    @ApiModelProperty(value = "返回结果")
    private String result;

}
