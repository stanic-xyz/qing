package chenyunlong.zhangli.common.service.impl;

import chenyunlong.zhangli.common.exception.ServiceException;
import chenyunlong.zhangli.mapper.AnimeFeedbackMapper;
import chenyunlong.zhangli.mapper.AnimeInfoMapper;
import chenyunlong.zhangli.model.entities.AnimeFeedbackEntity;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import chenyunlong.zhangli.common.service.ReportService;
import chenyunlong.zhangli.common.utils.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {
    private final AnimeFeedbackMapper feedbackMapper;
    private final AnimeInfoMapper animeInfoMapper;

    public ReportServiceImpl(AnimeFeedbackMapper feedbackMapper, AnimeInfoMapper animeInfoMapper) {
        this.feedbackMapper = feedbackMapper;
        this.animeInfoMapper = animeInfoMapper;
    }

    @Override
    public void addReport(String username, Long cid, boolean linkInvalid, boolean someMissing, boolean badQuality, String detail) {

        AnimeInfo animeInfo = animeInfoMapper.selectById(cid);
        if (animeInfo == null) {
            throw new ServiceException("动漫信息不存在");
        }

        AnimeFeedbackEntity entity = new AnimeFeedbackEntity();

        entity.setMid(cid);
        entity.setDetail(detail);
        entity.setType(1);
        entity.setDetail(StringUtils.isNotEmpty(detail) ? detail : "资源发生了异常，请检查");
        entity.setProcessingStatus(0);
        feedbackMapper.insert(entity);
    }
}
