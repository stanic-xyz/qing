package cn.chenyunlong.qing.domain.anime.anime.models;

public record Company(Long companyId, String companyName) {


    public static Company create(Long companyId, String companyName) {
        Company company = new Company(companyId, companyName);
        company.validate();
        return company;
    }

    // 校验公司信息是否合法
    public void validate() {
        if (companyId == null || companyId <= 0) {
            throw new IllegalArgumentException("公司ID不能为空且必须大于0");
        }
    }
}
