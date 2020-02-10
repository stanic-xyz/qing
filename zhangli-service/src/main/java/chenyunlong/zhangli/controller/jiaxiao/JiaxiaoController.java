package chenyunlong.zhangli.controller.jiaxiao;

import chenyunlong.zhangli.annotation.Email;
import chenyunlong.zhangli.annotation.JsonFieldFilter;
import chenyunlong.zhangli.entities.SchoolInfo;
import chenyunlong.zhangli.exception.MyException;
import chenyunlong.zhangli.model.ResultUtil;
import chenyunlong.zhangli.service.SchoolService;
import chenyunlong.zhangli.utils.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 这里是管理员的功能
 */
@Api
@Slf4j
@RestController
@RequestMapping("jiaxiao")
public class JiaxiaoController {
    @Autowired
    private SchoolService schoolService;

    @GetMapping("info")
    public String getSchoolInfo(String schoolId) {
        return "翔安驾校！";
    }

    @ApiOperation("添加学校信息")
    @PostMapping("add")
    public String addSchoolInfo(@RequestBody SchoolInfo schoolInfo) {
        schoolService.addSchoolInfo(schoolInfo);
        return "add Success!";
    }

    @ApiOperation("删除学校信息")
    @DeleteMapping("delete")
    public String deleteSchoolInfo(@RequestParam Long schoolId) {
        schoolService.deleteSchoolInfo(schoolId);
        return "delete Success";
    }

    @ApiOperation("修改学校信息")
    @PutMapping("modify")
    public String modifySchoolInfo(SchoolInfo schoolInfo) {
        schoolService.modifySchoolInfo(schoolInfo);
        return "modify successFully";
    }

    @ApiOperation("查询所有学校信息")
    @GetMapping("list")
    public List<SchoolInfo> list() {
        return schoolService.getAllSchoolInfo();
    }

    @Email(receiver = "1576302867@qq.com", content = "消息内容", object = "邮件主题")
    @GetMapping("testExp")
    @JsonFieldFilter
    public BaseResponse testException(@RequestParam String code) {
        return ResultUtil.success(code);
    }
}
