package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.dao.SchoolInfoReposutory;
import chenyunlong.zhangli.entities.SchoolInfo;
import chenyunlong.zhangli.service.SchoolService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    private SchoolInfoReposutory schoolInfoReposutory;

    @Override
    public int addSchoolInfo(SchoolInfo schoolInfo) {
        Optional<SchoolInfo> exits = schoolInfoReposutory.findById(schoolInfo.getSchoolId());
        if (exits.isPresent()) {
            return 0;
        } else {
            SchoolInfo saved = schoolInfoReposutory.save(schoolInfo);
        }
        return 0;
    }

    @Override
    public void deleteSchoolInfo(Long schoolId) {
        Optional<SchoolInfo> schoolInfo = schoolInfoReposutory.findById(schoolId);
        if (schoolInfo.isPresent()) {
            schoolInfoReposutory.delete(schoolInfo.get());
        }
    }

    @Override
    public int modifySchoolInfo(SchoolInfo schoolInfo) {
        Optional<SchoolInfo> oldSchoolInfo = schoolInfoReposutory.findById(schoolInfo.getSchoolId());
        if (oldSchoolInfo.isPresent()) {
            //说明了之前的存在
            SchoolInfo schoolInfo1 = oldSchoolInfo.get();
            schoolInfo1.setSchoolCode(schoolInfo.getSchoolCode());
            schoolInfo1.setSchoolName(schoolInfo.getSchoolName());
            schoolInfoReposutory.save(schoolInfo1);
            return 0;
        } else {
            return 1;
        }

    }

    @Override
    public List<SchoolInfo> getAllSchoolInfo() {
        return schoolInfoReposutory.findAll();
    }
}
