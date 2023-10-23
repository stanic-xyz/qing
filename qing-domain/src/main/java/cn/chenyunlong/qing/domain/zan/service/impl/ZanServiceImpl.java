// ---Auto Generated by Qing-Generator --

package cn.chenyunlong.qing.domain.zan.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaService;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.jpa.support.domain.BaseEntity;
import cn.chenyunlong.qing.domain.entity.Entity;
import cn.chenyunlong.qing.domain.entity.repository.EntityRepository;
import cn.chenyunlong.qing.domain.user.User;
import cn.chenyunlong.qing.domain.user.repository.UserRepository;
import cn.chenyunlong.qing.domain.zan.LikeContext;
import cn.chenyunlong.qing.domain.zan.LikeModel;
import cn.chenyunlong.qing.domain.zan.Zan;
import cn.chenyunlong.qing.domain.zan.creator.ZanCreator;
import cn.chenyunlong.qing.domain.zan.mapper.ZanMapper;
import cn.chenyunlong.qing.domain.zan.pipeline.BizEnum;
import cn.chenyunlong.qing.domain.zan.pipeline.EventFilterChain;
import cn.chenyunlong.qing.domain.zan.pipeline.FilterChainPipeline;
import cn.chenyunlong.qing.domain.zan.pipeline.selecter.FilterSelector;
import cn.chenyunlong.qing.domain.zan.query.ZanQuery;
import cn.chenyunlong.qing.domain.zan.repository.ZanRepository;
import cn.chenyunlong.qing.domain.zan.request.ZanCreateRequest;
import cn.chenyunlong.qing.domain.zan.service.IZanService;
import cn.chenyunlong.qing.domain.zan.service.plugin.SmsPlugin;
import cn.chenyunlong.qing.domain.zan.service.selector.LikeFilterSelectorFactory;
import cn.chenyunlong.qing.domain.zan.updater.ZanUpdater;
import cn.chenyunlong.qing.domain.zan.vo.ZanVO;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
@EnablePluginRegistries(value = {SmsPlugin.class})
public class ZanServiceImpl extends BaseJpaService implements IZanService {
    private final ZanRepository zanRepository;
    private final EntityRepository entityRepository;
    private final UserRepository userRepository;
    private final FilterChainPipeline<LikeContext> likeContextPipeline;
    private final LikeFilterSelectorFactory likeFilterSelectorFactory;
    private final PluginRegistry<SmsPlugin, LikeModel> pluginRegistry;

    /**
     * 点赞
     *
     * @param createRequest 请求参数
     * @return 点赞结果
     */
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
                likeModelLikePlugin.sendSms(likeModel.getUser().getPhone(),
                    likeModel.getUser().getEmail()));

        // 业务处理完毕，读取处理结果
        return likeContext.getCreateRequest().getEntityId();
    }

    /**
     * createImpl
     */
    @Override
    public Long createZan(ZanCreator creator) {
        Optional<Entity> entity = entityRepository.findById(creator.getEntityId());
        if (entity.isEmpty()) {
            throw new BusinessException("创建zan失败，实体不存在");
        }
        Optional<User> userRepositoryById = userRepository.findById(creator.getUserId());
        if (userRepositoryById.isEmpty()) {
            throw new BusinessException("创建zan失败，用户不存在");
        }
        Optional<Zan> zan = EntityOperations.doCreate(zanRepository)
            .create(() -> ZanMapper.INSTANCE.dtoToEntity(creator))
            .update(Zan::init)
            .execute();
        return zan.isPresent() ? zan.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateZan(ZanUpdater updater) {
        doUpdate(zanRepository)
            .loadById(updater.getId())
            .update(updater::updateZan)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validZan(Long id) {
        doUpdate(zanRepository)
            .loadById(id)
            .update(BaseEntity::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidZan(Long id) {
        doUpdate(zanRepository)
            .loadById(id)
            .update(BaseEntity::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public ZanVO findById(Long id) {
        Optional<Zan> zan = zanRepository.findById(id);
        return new ZanVO(zan.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<ZanVO> findByPage(PageRequestWrapper<ZanQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return zanRepository.findAll(pageRequest).map(ZanVO::new);
    }
}