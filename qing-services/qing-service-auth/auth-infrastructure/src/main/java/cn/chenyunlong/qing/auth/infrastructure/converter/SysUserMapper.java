package cn.chenyunlong.qing.auth.infrastructure.converter;


import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.auth.domain.user.QingUser;
import cn.chenyunlong.qing.auth.domain.user.dto.vo.QingUserVO;
import cn.chenyunlong.qing.auth.domain.user.dto.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CustomMapper.class, DateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysUserMapper {

    SysUserMapper INSTANCE = Mappers.getMapper(SysUserMapper.class);

    QingUserVO entityToVo(QingUser qingUser);

    UserVO entityToUserVo(QingUser qingUser);
}
