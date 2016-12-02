package com.lianjia.cs.dubbo.config.springboot.entity;

import com.lianjia.cs.dubbo.config.springboot.Idable;

/**
 * Created by chengtianliang on 2016/11/30.
 */
public class ApplicationProperty implements Idable {

    private String id;

    private String name;

    private String owner;

    private String organization;

    private String architecture;

    private String environment;

    private String compiler;

    private String logger;

    private boolean defaultApp;

    private String version;

    public String getId() {
        return id == null ? name : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? id : name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getCompiler() {
        return compiler;
    }

    public void setCompiler(String compiler) {
        this.compiler = compiler;
    }

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public boolean isDefaultApp() {
        return defaultApp;
    }

    public void setDefaultApp(boolean defaultApp) {
        this.defaultApp = defaultApp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ApplicationProperty{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", organization='" + organization + '\'' +
                ", architecture='" + architecture + '\'' +
                ", environment='" + environment + '\'' +
                ", compiler='" + compiler + '\'' +
                ", logger='" + logger + '\'' +
                ", defaultApp=" + defaultApp +
                '}';
    }
}
