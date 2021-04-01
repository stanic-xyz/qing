package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.model.entities.AnimeType;
import chenyunlong.zhangli.mapper.AnimeTypeMapper;
import chenyunlong.zhangli.service.AnimeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stan
 */
@Service
public class AnimeTypeServiceImpl implements AnimeTypeService {
    @Autowired
    private AnimeTypeMapper animeTypeMapper;

    @Override
    public List<AnimeType> getAllTypeInfo() {
        return animeTypeMapper.listTypes();
    }

    @Override
    public void addAnimeType(AnimeType animeType) {
        animeTypeMapper.addAnimeType(animeType);
    }
}
