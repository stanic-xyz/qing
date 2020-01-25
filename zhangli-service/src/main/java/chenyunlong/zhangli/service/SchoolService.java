package chenyunlong.zhangli.service;


import chenyunlong.zhangli.entities.SchoolInfo;

import java.util.List;

public interface SchoolService {

    int addSchoolInfo(SchoolInfo schoolInfo);

    int deleteSchoolInfo(Long schoolId);

    int modifySchoolInfo(SchoolInfo schoolInfo);

    List<SchoolInfo> getAllSchoolInfo();
}
