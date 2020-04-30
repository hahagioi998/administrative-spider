package com.mingrn.spider.admin;

import java.io.Serializable;
import java.util.List;

public class Node implements Serializable {

    private static final long serialVersionUID = -5563495138350426982L;

    private String name;

    private String code;

    private String desc;

    private List<Node> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                ", children=" + children +
                '}';
    }
}
