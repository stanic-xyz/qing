package cn.chenyunlong.zhangli.cache;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Cache wrapper.
 *
 * @author johnniang
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
class CacheWrapper<V> implements Serializable {

    /**
     * Cache data
     */
    private V data;

    /**
     * Expired time.
     */
    private LocalDateTime expireAt;

    /**
     * Create time.
     */
    private LocalDateTime createAt;
}
