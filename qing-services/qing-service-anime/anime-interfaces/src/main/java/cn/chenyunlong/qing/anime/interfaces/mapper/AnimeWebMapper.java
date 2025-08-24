package cn.chenyunlong.qing.anime.interfaces.mapper;

import cn.chenyunlong.qing.anime.application.command.AnimeStatusCommand;
import cn.chenyunlong.qing.anime.application.command.CreateAnimeCommand;
import cn.chenyunlong.qing.anime.application.command.UpdateAnimeCommand;
import cn.chenyunlong.qing.anime.application.dto.AnimeDTO;
import cn.chenyunlong.qing.anime.application.dto.PageResult;
import cn.chenyunlong.qing.anime.application.query.AnimeQuery;
import cn.chenyunlong.qing.anime.interfaces.dto.request.AnimeCreateRequest;
import cn.chenyunlong.qing.anime.interfaces.dto.request.AnimeQueryRequest;
import cn.chenyunlong.qing.anime.interfaces.dto.request.AnimeStatusRequest;
import cn.chenyunlong.qing.anime.interfaces.dto.request.AnimeUpdateRequest;
import cn.chenyunlong.qing.anime.interfaces.dto.response.AnimeResponse;
import cn.chenyunlong.qing.anime.interfaces.dto.response.PageResponse;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 动漫Web层映射器
 *
 * <p>负责Web层DTO与应用层Command/Query/DTO之间的转换</p>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@Component
public interface AnimeWebMapper {

    /**
     * 创建请求转换为创建命令
     *
     * @param request 创建请求
     * @return 创建命令
     */
    CreateAnimeCommand toCreateCommand(AnimeCreateRequest request);

    /**
     * 更新请求转换为更新命令
     *
     * @param animeId 动漫ID
     * @param request 更新请求
     * @return 更新命令
     */
    UpdateAnimeCommand toUpdateCommand(Long animeId, AnimeUpdateRequest request);

    /**
     * 状态请求转换为状态命令
     *
     * @param animeId 动漫ID
     * @param request 状态请求
     * @return 状态命令
     */
    @Mapping(target = "operation", expression = "java(parseStatusOperation(request.getOperation()))")
    AnimeStatusCommand toStatusCommand(Long animeId, AnimeStatusRequest request);

    /**
     * 查询请求转换为查询对象
     *
     * @param request 查询请求
     * @return 查询对象
     */
    AnimeQuery toQuery(AnimeQueryRequest request);

    /**
     * 动漫DTO转换为响应对象
     *
     * @param dto 动漫DTO
     * @return 响应对象
     */
    AnimeResponse toResponse(AnimeDTO dto);

    /**
     * 动漫DTO列表转换为响应对象列表
     *
     * @param dtoList 动漫DTO列表
     * @return 响应对象列表
     */
    List<AnimeResponse> toResponseList(List<AnimeDTO> dtoList);

    /**
     * 分页结果转换为分页响应
     *
     * @param pageResult 分页结果
     * @return 分页响应
     */
    default PageResponse<AnimeResponse> toPageResponse(PageResult<AnimeDTO> pageResult) {
        if (pageResult == null) {
            return PageResponse.empty(1, 20);
        }
        List<AnimeResponse> content = toResponseList(pageResult.getData());
        return PageResponse.of(
                content,
                pageResult.getPage(),
                pageResult.getSize(),
                pageResult.getTotal()
        );
    }

    /**
     * 解析状态操作类型
     *
     * @param operation 操作字符串
     * @return 状态操作枚举
     */
    default AnimeStatusCommand.StatusOperation parseStatusOperation(String operation) {
        if (operation == null) {
            return null;
        }
        try {
            return AnimeStatusCommand.StatusOperation.valueOf(operation.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("不支持的操作类型: " + operation);
        }
    }

    /**
     * 更新创建命令的清理字段
     *
     * @param command 创建命令
     * @param request 创建请求
     */
    default void updateCreateCommand(@MappingTarget CreateAnimeCommand command, AnimeCreateRequest request) {
        if (request.getName() != null) {
            command.setName(request.getName().trim());
        }
        if (request.getInstruction() != null) {
            command.setInstruction(request.getInstruction().trim());
        }
    }

    /**
     * 更新更新命令的清理字段
     *
     * @param command 更新命令
     * @param request 更新请求
     */
    default void updateUpdateCommand(@MappingTarget UpdateAnimeCommand command, AnimeUpdateRequest request) {
        if (request.getName() != null) {
            command.setName(request.getName().trim());
        }
        if (request.getInstruction() != null) {
            command.setInstruction(request.getInstruction().trim());
        }
    }
}
