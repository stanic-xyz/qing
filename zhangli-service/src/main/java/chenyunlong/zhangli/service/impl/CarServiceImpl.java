package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.dao.CarRepository;
import chenyunlong.zhangli.entities.Car;
import chenyunlong.zhangli.exception.MyException;
import chenyunlong.zhangli.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {


    @Autowired
    private CarRepository carRepository;

    @Override
    public List<Car> list() {
        return carRepository.findAll();
    }

    @Override
    public Car findCarById(Long carId) {
        Optional<Car> one = carRepository.findById(carId);
        return one.orElse(null);
    }

    @Override
    public List<Car> findCarByCarNumber(String carNumber) {
        Car car = new Car();
        car.setCarNumber(carNumber);
        Example<Car> example = Example.of(car);
        return carRepository.findAll(example);
    }

    @Override
    public Car save(Car car) {
        return carRepository.save(car);
    }


    @Override
    public void deleteById(Long carId) throws MyException {
        try {
            carRepository.deleteById(carId);
        } catch (Exception exp) {
            throw new MyException(exp.getMessage(), -1);
        }
    }

    @Override
    public void modifyCar(Car car) throws MyException {
        Optional<Car> oldCar = carRepository.findById(car.getId());
        if (oldCar.isPresent()) {
            Car oldcCarInfo = oldCar.get();
            oldcCarInfo.setCarNumber(car.getCarNumber());
            oldcCarInfo.setCarImg(car.getCarImg());
            oldcCarInfo.setCarStatus(car.getCarStatus());
        } else {
            throw new MyException("车辆不存在", -1);
        }
    }
}
