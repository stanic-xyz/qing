package chenyunlong.zhangli.service.base;

import chenyunlong.zhangli.core.exception.NotFoundException;
import chenyunlong.zhangli.core.exception.ServiceException;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Abstract service implementation.
 *
 * @param <DOMAIN> domain type
 * @author johnniang
 */
@Slf4j
public abstract class AbstractCrudService<MAPPER extends BaseMapper<DOMAIN>, DOMAIN> extends ServiceImpl<MAPPER, DOMAIN> implements CrudService<DOMAIN> {

    private final String domainName;
    private final String mapperName;

    @SuppressWarnings("unchecked")
    protected AbstractCrudService() {
        // Get domain name
        Class<MAPPER> mapperClass = (Class<MAPPER>) fetchType(0);
        Class<DOMAIN> domainClass = (Class<DOMAIN>) fetchType(1);
        domainName = domainClass.getSimpleName();
        mapperName = mapperClass.getSimpleName();
    }

    /**
     * Gets actual generic type.
     *
     * @param index generic type index
     * @return real generic type will be returned
     */
    private Type fetchType(int index) {
        Assert.isTrue(index >= 0 && index <= 1, "type index must be between 0 to 1");
        return ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[index];
    }


    /**
     * Exists by id.
     *
     * @param id id
     * @return boolean
     */
    @Override
    public boolean existsById(@NonNull Serializable id) {
        Assert.notNull(id, domainName + " id must not be null");
        return getById(id) == null;
    }

    /**
     * Must exist by id, or throw NotFoundException.
     *
     * @param id id
     * @throws NotFoundException If the specified id does not exist
     */
    @Override
    public DOMAIN mustExistById(@NonNull Serializable id) {
        DOMAIN domain = getById(id);
        if (domain == null) {
            throw new NotFoundException(domainName + " was not exist");
        } else {
            return domain;
        }
    }

    /**
     * save by domain
     *
     * @param domain domain
     * @return DOMAIN
     */
    @Override
    public DOMAIN create(@NonNull DOMAIN domain) {
        Assert.notNull(domain, domainName + " data must not be null");
        boolean save = save(domain);
        if (save) {
            return domain;
        } else {
            throw new ServiceException("未知异常，保存失败,错误可能存在于" + mapperName + "中");
        }
    }

    /**
     * Updates by domain
     *
     * @param domain domain
     * @return DOMAIN
     */
    @Override
    public boolean updateBy(@NonNull DOMAIN domain) {
        Assert.notNull(domain, domainName + " data must not be null");
        return updateById(domain);
    }


    /**
     * Updates by domains
     *
     * @param domains domains
     * @return List
     */
    @Override
    public boolean updateInBatch(@NonNull Collection<DOMAIN> domains) {
        return !CollectionUtils.isEmpty(domains) && updateBatchById(domains);
    }


    /**
     * Remove by domain
     *
     * @param domain domain
     */
    @Override
    public void remove(@NonNull DOMAIN domain) {
        Assert.notNull(domain, domainName + " data must not be null");
        removeById(domain);
    }

    /**
     * Remove by ids
     *
     * @param ids ids
     */
    @Override
    public void removeInBatch(@NonNull Collection<Object> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            log.debug(domainName + " id collection is empty");
            return;
        }
        removeBatchByIds(ids);
    }
}
