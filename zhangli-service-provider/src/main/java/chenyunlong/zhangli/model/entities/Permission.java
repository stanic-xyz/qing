package chenyunlong.zhangli.model.entities;

import java.io.Serializable;

public class Permission implements Serializable {

    public Permission() {

    }

    public Permission(String name, String id, String description) {
        this.name = name;
        this.id = id;
        this.description = description;
    }

    private String name;
    private String id;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Permisssion{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
