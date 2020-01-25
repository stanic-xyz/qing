package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.dao.SchoolInfoReposutory;
import chenyunlong.zhangli.entities.SchoolInfo;
import chenyunlong.zhangli.service.SchoolService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public int deleteSchoolInfo(Long schoolId) {
        return 0;
    }

    @Override
    public int modifySchoolInfo(SchoolInfo schoolInfo) {
        return 0;
    }

    @Override
    public List<SchoolInfo> getAllSchoolInfo() {
        return schoolInfoReposutory.findAll();
    }
}
