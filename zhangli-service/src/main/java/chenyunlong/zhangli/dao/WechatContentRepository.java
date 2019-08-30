package chenyunlong.zhangli.dao;

import chenyunlong.zhangli.entities.WechatContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


@Component
public interface WechatContentRepository extends JpaRepository<WechatContent, Long> {

}
