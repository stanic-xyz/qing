package chenyunlong.zhangli.service;


import chenyunlong.zhangli.entities.SchoolInfo;

import java.util.List;

public interface SchoolService {

    /**
     * 添加学校信息
     *
     * @param schoolInfo
     * @return
     */
    int addSchoolInfo(SchoolInfo schoolInfo);

    /**
     * 删除学校信息
     *
     * @param schoolId
     */
    void deleteSchoolInfo(Long schoolId);

    /**
     * 修改学校信息
     *
     * @param schoolInfo
     * @return
     */
    int modifySchoolInfo(SchoolInfo schoolInfo);

    /**
     * 查询所有的学校信息
     *
     * @return
     */
    List<SchoolInfo> getAllSchoolInfo();
}
