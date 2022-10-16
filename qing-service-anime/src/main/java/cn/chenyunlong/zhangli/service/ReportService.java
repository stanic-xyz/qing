package cn.chenyunlong.zhangli.service;

/**
 * @author Stan
 */
public interface ReportService {
    /**
     * 添加反馈信息
     *
     * @param username    用户名
     * @param cid         动漫ID
     * @param linkInvalid 链接无效
     * @param someMissing 内容确实
     * @param badQuality  质量果茶
     * @param detail      其他详情
     */
    void addReport(String username, Long cid, boolean linkInvalid, boolean someMissing, boolean badQuality, String detail);
}
