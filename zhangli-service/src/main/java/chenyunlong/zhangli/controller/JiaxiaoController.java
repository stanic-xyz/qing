package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.entities.SchoolInfo;
import chenyunlong.zhangli.service.SchoolService;
import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 这里是管理员的功能
 */
@Api
@RestController
@RequestMapping("jiaxiao")
public class JiaxiaoController {
    @Autowired
    private SchoolService schoolService;

    @GetMapping("info")
    public String getSchoolInfo(String schoolId) {
        return "翔安驾校！";
    }

    @ApiOperation("添加学校继续你想")
    @PostMapping("add")
    public String addSchoolInfo(@RequestBody SchoolInfo schoolInfo) {
        schoolService.addSchoolInfo(schoolInfo);
        return "add Success!";
    }

    @DeleteMapping("delete")
    public String deleteSchoolInfo(Long schoolId) {
        schoolService.deleteSchoolInfo(schoolId);
        return "delete Success";
    }

    @PutMapping("modify")
    public String modifySchoolInfo(SchoolInfo schoolInfo) {
        schoolService.modifySchoolInfo(schoolInfo);
        return "modify successFully";
    }

    @GetMapping("list")
    public List<SchoolInfo> list() {
        return schoolService.getAllSchoolInfo();
    }
}
