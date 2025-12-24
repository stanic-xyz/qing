package cn.chenyunlong.codegen.example.domain.service;

import cn.chenyunlong.codegen.example.domain.dto.creator.UserCreator;
import cn.chenyunlong.codegen.example.domain.dto.query.UserQuery;
import cn.chenyunlong.codegen.example.domain.dto.updater.UserUpdater;
import cn.chenyunlong.codegen.example.domain.dto.vo.UserVO;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.common.AggregateId;
import java.lang.Long;
import org.springframework.data.domain.Page;

public interface IUserService {
    /**
     * create
     */
    Long createUser(UserCreator creator);

    /**
     * update
     */
    void updateUser(UserUpdater updater);

    /**
     * valid
     */
    void validUser(AggregateId id);

    /**
     * invalid
     */
    void invalidUser(AggregateId id);

    /**
     * findById
     */
    UserVO findById(AggregateId id);

    /**
     * findByPage
     */
    Page<UserVO> findByPage(PageRequestWrapper<UserQuery> query);
}
