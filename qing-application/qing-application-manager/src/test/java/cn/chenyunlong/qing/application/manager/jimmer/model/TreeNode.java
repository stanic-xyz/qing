package cn.chenyunlong.qing.application.manager.jimmer.model;

import org.babyfish.jimmer.sql.Column;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.GeneratedValue;
import org.babyfish.jimmer.sql.GenerationType;
import org.babyfish.jimmer.sql.Id;

@Entity
public interface TreeNode {

    @Id
    @Column(name = "NODE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    String name();
}
