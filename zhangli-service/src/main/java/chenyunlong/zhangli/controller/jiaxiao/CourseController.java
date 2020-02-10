package chenyunlong.zhangli.controller.jiaxiao;

import chenyunlong.zhangli.entities.Course;
import chenyunlong.zhangli.exception.MyException;
import chenyunlong.zhangli.model.ResultUtil;
import chenyunlong.zhangli.service.CourseService;
import chenyunlong.zhangli.utils.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("课程控制器")
@RestController
@RequestMapping("course")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("list")
    public BaseResponse list() {
        return ResultUtil.success(courseService.list());
    }

    @PostMapping("add")
    public BaseResponse add(@RequestBody Course course) throws MyException {
        return ResultUtil.success(courseService.add(course));
    }

    @DeleteMapping("{courseId}/delete")
    public BaseResponse delete(@PathVariable Long courseId) {
        courseService.delete(courseId);
        return ResultUtil.success("删除成功!");
    }
}
