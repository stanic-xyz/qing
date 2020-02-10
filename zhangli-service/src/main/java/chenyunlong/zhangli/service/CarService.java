package chenyunlong.zhangli.service;

import chenyunlong.zhangli.entities.Car;
import chenyunlong.zhangli.exception.MyException;

import java.util.List;

public interface CarService {
    /**
     * 查询所有车辆信息
     *
     * @return
     */
    List<Car> list();

    /**
     * 根据ID车讯车辆信息
     *
     * @param carId
     * @return
     */
    Car findCarById(Long carId);

    /**
     * 根据车牌号查询车辆信息
     *
     * @param carNumber
     * @return
     */
    List<Car> findCarByCarNumber(String carNumber);

    /**
     * 添加车辆信息
     *
     * @param car
     * @return
     */
    Car save(Car car);

    /**
     * 删除指定的车辆信息，如果该车辆有相关的课程信息的话，需要对课程信息进行处理
     *
     * @param carId
     */
    void deleteById(Long carId) throws MyException;

    /**
     * 修改车辆信息
     *
     * @param car
     */
    void modifyCar(Car car) throws MyException;
}
