package chenyunlong.zhangli.common.core.support;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

/**
 * @author ryanwang
 * @date 2020-03-06
 */
@Data
public class Pagination {

    private Long totalCount;

    private Long pages;

    private Long size;

    private Long current;

    private List<RainbowPage> rainbowPages;

    private String nextPageFullPath;

    private String prevPageFullPath;

    private Boolean hasPrev;

    private Boolean hasNext;

    public Pagination(IPage animeInfoPage) {
        this.pages = animeInfoPage.getPages();
        this.totalCount = animeInfoPage.getTotal();
        this.size = animeInfoPage.getSize();
        this.hasNext = animeInfoPage.getCurrent() < animeInfoPage.getPages();
        this.hasPrev = animeInfoPage.getCurrent() > 1;
        this.current = animeInfoPage.getCurrent();
    }
}
