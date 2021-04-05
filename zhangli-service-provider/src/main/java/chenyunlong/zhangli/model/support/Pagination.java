package chenyunlong.zhangli.model.support;

import chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
