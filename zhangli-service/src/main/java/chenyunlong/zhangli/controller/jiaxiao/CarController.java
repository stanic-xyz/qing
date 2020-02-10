package chenyunlong.zhangli.controller.jiaxiao;

import chenyunlong.zhangli.entities.Car;
import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.exception.MyException;
import chenyunlong.zhangli.model.ResultUtil;
import chenyunlong.zhangli.service.CarService;
import chenyunlong.zhangli.service.UserService;
import chenyunlong.zhangli.utils.BaseResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("jiaxiao/car")
public class CarController {

    @Autowired
    private CarService carService;
    @Autowired
    private UserService userService;

    @ApiOperation("获取所有车辆信息")
    @GetMapping("list")
    public BaseResponse list() {
        List<Car> carList = carService.list();
        return ResultUtil.success(carList);
    }

    @ApiOperation("查询车辆信息")
    @GetMapping("{carId}/get")
    public BaseResponse findCar(@PathVariable Long carId) {
        return ResultUtil.success(carService.findCarById(carId));
    }

    @GetMapping("find")
    public BaseResponse findCarByCarNumer(@RequestParam String carNumber) {
        List<Car> carList = carService.findCarByCarNumber(carNumber);
        return ResultUtil.success(carList);
    }

    /**
     * 添加參數校驗
     *
     * @param car
     * @param bindingResult
     * @return
     */
    @ApiOperation("添加一条车辆信息")
    @PostMapping("add")
    public BaseResponse addCar(@Validated @RequestBody Car car, BindingResult bindingResult) {


        if (!bindingResult.hasErrors()) {
            User user = userService.findUserByUserId(car.getUser().getUserID());
            if (user == null) {
                return ResultUtil.fail("用户不存在");
            }
            car.setUser(user);
            return ResultUtil.success(carService.save(car));
        } else {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            String errorMessage = "";
            ObjectError error;
            for (Iterator var5 = errorList.iterator(); var5.hasNext(); errorMessage = errorMessage + error.getDefaultMessage() + ",") {
                error = (ObjectError) var5.next();
            }
            return ResultUtil.fail(errorMessage.substring(0, errorMessage.length() - 1));
        }
    }

    /**
     * 删除车辆信息
     *
     * @param carId 车辆ID
     * @return
     */
    @DeleteMapping("{car}/delete")
    private BaseResponse deleteCar(@PathVariable Long carId) throws MyException {
        carService.deleteById(carId);
        return ResultUtil.success("success");
    }

    /**
     * 修改车辆信息
     *
     * @param car
     * @return
     */
    @PutMapping("modify")
    public BaseResponse modifyCar(@RequestBody Car car) throws MyException {
        carService.modifyCar(car);
        return ResultUtil.success("success");
    }
}
