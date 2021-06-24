package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.model.entities.AnimeType;
import chenyunlong.zhangli.mapper.AnimeTypeMapper;
import chenyunlong.zhangli.model.params.AnimeTypeParam;
import chenyunlong.zhangli.service.AnimeTypeService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stan
 */
@Service
public class AnimeTypeServiceImpl implements AnimeTypeService {
    private final AnimeTypeMapper animeTypeMapper;

    public AnimeTypeServiceImpl(AnimeTypeMapper animeTypeMapper) {
        this.animeTypeMapper = animeTypeMapper;
    }

    @Override
    public List<AnimeType> getAllTypeInfo() {
        return animeTypeMapper.listTypes();
    }

    @Override
    public AnimeType addAnimeType(AnimeType animeType) {
        animeType.preCheck();
        animeTypeMapper.addAnimeType(animeType);
        return animeType;
    }

    @Override
    public AnimeType getById(Long typeId) {
        return animeTypeMapper.selectById(typeId);
    }

    @Override
    public AnimeType update(AnimeType typeInfo) {
        animeTypeMapper.updateById(typeInfo);
        return typeInfo;
    }

    @Override
    public IPage<AnimeType> pageBy(AnimeTypeParam typeParam, Pageable pageable) {
        Page<AnimeType> animeTypePage = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
        LambdaQueryWrapper<AnimeType> queryWrapper = new LambdaQueryWrapper<>();
        return animeTypeMapper.selectPage(animeTypePage, queryWrapper);
    }
}
