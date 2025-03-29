package cn.chenyunlong.qing.application.manager.jimmer.model;

import org.babyfish.jimmer.sql.*;

@Entity
public interface TreeNode {

    @Id
    @Column(name = "NODE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    String name();
}
