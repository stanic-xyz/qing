package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.dao.CarRepository;
import chenyunlong.zhangli.dao.CourseRepository;
import chenyunlong.zhangli.dao.UserRepository;
import chenyunlong.zhangli.entities.Car;
import chenyunlong.zhangli.entities.Course;
import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.exception.MyException;
import chenyunlong.zhangli.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, UserRepository userRepository, CarRepository carRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
    }

    @Override
    public List<Course> list() {
        return courseRepository.findAll();
    }

    @Override
    public Course add(Course course) throws MyException {
        Optional<User> user = userRepository.findById(course.getUser().getUserID());
        if (!user.isPresent()) {
            throw new MyException("用户不存在", 1);
        }
        Optional<Car> car = carRepository.findById(course.getCar().getId());
        if (!car.isPresent()) {
            throw new MyException("车辆不存在", 1);
        }
        return courseRepository.save(course);
    }

    @Override
    public void delete(Long courseId) {
        courseRepository.deleteById(courseId);
    }
}
