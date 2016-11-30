package com.lianjia.dubbo.config.springboot.entity;

import com.lianjia.dubbo.config.springboot.Idable;

/**
 * Created by chengtianliang on 2016/11/29.
 */
public class ModuleProperty implements Idable{
    private String id;

    private String name;

    private String version;

    private String owner;

    private String organization;

    private boolean defaultModule;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @Override
    public String getId() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDefaultModule() {
        return defaultModule;
    }

    public void setDefaultModule(boolean defaultModule) {
        this.defaultModule = defaultModule;
    }

    @Override
    public String toString() {
        return "ModuleProperty{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", owner='" + owner + '\'' +
                ", organization='" + organization + '\'' +
                ", defaultModule=" + defaultModule +
                '}';
    }
}
