package cn.chenyunlong.qing.domain.zan.service.impl;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.qing.domain.auth.user.repository.UserRepository;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import cn.chenyunlong.qing.domain.zan.LikeContext;
import cn.chenyunlong.qing.domain.zan.LikeModel;
import cn.chenyunlong.qing.domain.zan.Zan;
import cn.chenyunlong.qing.domain.zan.dto.creator.ZanCreator;
import cn.chenyunlong.qing.domain.zan.dto.request.ZanCreateRequest;
import cn.chenyunlong.qing.domain.zan.dto.updater.ZanUpdater;
import cn.chenyunlong.qing.domain.zan.mapper.ZanMapper;
import cn.chenyunlong.qing.domain.zan.pipeline.BizEnum;
import cn.chenyunlong.qing.domain.zan.pipeline.EventFilterChain;
import cn.chenyunlong.qing.domain.zan.pipeline.FilterChainPipeline;
import cn.chenyunlong.qing.domain.zan.pipeline.selecter.FilterSelector;
import cn.chenyunlong.qing.domain.zan.repository.ZanRepository;
import cn.chenyunlong.qing.domain.zan.service.IZanService;
import cn.chenyunlong.qing.domain.zan.service.plugin.SmsPlugin;
import cn.chenyunlong.qing.domain.zan.service.selector.LikeFilterSelectorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
@EnablePluginRegistries({SmsPlugin.class})
public class ZanServiceImpl implements IZanService {

    private final ZanRepository zanRepository;
    private final UserRepository userRepository;
    private final FilterChainPipeline<LikeContext> likeContextPipeline;
    private final LikeFilterSelectorFactory likeFilterSelectorFactory;
    private final PluginRegistry<SmsPlugin, LikeModel> pluginRegistry;

    /**
     * createImpl
     */
    @Override
    public Long createZan(ZanCreator creator) {
        userRepository.findById(new AggregateId(creator.getUserId())).orElseThrow(() -> new BusinessException("创建zan失败，用户不存在"));
        Optional<Zan> zan = EntityOperations.doCreate(zanRepository)
            .create(() -> ZanMapper.INSTANCE.dtoToEntity(creator))
            .update(Zan::init)
            .execute();
        return zan.isPresent() ? zan.get().getAggregateId().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateZan(ZanUpdater updater) {
        EntityOperations.doUpdate(zanRepository)
            .loadById(new AggregateId(updater.getId()))
            .update(updater::updateZan)
            .successHook((content) -> log.info("更新点赞记录{}成功！", content.getAggregateId()))
            .execute();
    }

    @Override
    public void validZan(Long id) {
        EntityOperations.doUpdate(zanRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::valid)
            .execute();
    }

    @Override
    public void invalidZan(Long id) {
        EntityOperations.doUpdate(zanRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::invalid)
            .execute();
    }

    @Override
    public Long like(ZanCreateRequest createRequest) {
        FilterSelector filterSelector = likeFilterSelectorFactory.getFilterSelector(BizEnum.LIKE);
        LikeContext likeContext = LikeContext.create(BizEnum.LIKE, filterSelector);
        likeContext.setCreateRequest(createRequest);
        EventFilterChain<LikeContext> filterChain = likeContextPipeline.getNext();
        filterChain.handle(likeContext);

        LikeModel likeModel = likeContext.getLikeModel();
        // 通过插件处理业务扩展
        pluginRegistry.getPluginsFor(likeModel).forEach(
            likeModelLikePlugin ->
                likeModelLikePlugin.sendSms(likeModel.getQingUser().getPhone(),
                    likeModel.getQingUser().getEmail()));

        // 业务处理完毕，读取处理结果
        return likeContext.getCreateRequest().getEntityId();
    }
}
