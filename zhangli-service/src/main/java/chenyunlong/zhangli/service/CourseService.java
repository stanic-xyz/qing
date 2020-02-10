package chenyunlong.zhangli.service;

import chenyunlong.zhangli.entities.Course;
import chenyunlong.zhangli.exception.MyException;

import java.util.List;

public interface CourseService {
    /**
     * c查询所有的课程信息
     *
     * @return
     */
    List<Course> list();

    Course add(Course course) throws MyException;

    void delete(Long courseId);
}
