package cn.chenyunlong.qing.domain.auth.user.converter;


import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.domain.auth.user.QingUser;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.QingUserVO;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.UserVO;
import cn.chenyunlong.qing.infrustructure.converter.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CustomMapper.class, DateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysUserMapper {

    SysUserMapper INSTANCE = Mappers.getMapper(SysUserMapper.class);

    QingUserVO entityToVo(QingUser qingUser);

    UserVO entityToUserVo(QingUser qingUser);
}
