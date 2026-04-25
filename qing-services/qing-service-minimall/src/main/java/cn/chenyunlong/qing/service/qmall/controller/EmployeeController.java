package cn.chenyunlong.qing.service.qmall.controller;

import cn.chenyunlong.qing.service.qmall.entity.Employee;
import cn.chenyunlong.qing.service.qmall.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * 执行转账
     * GET /api/employees/transfer?fromId=1&toId=2&amount=100
     */
    @GetMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transfer(
            @RequestParam Long fromId,
            @RequestParam Long toId,
            @RequestParam BigDecimal amount) {

        Map<String, Object> response = new HashMap<>();
        try {
            employeeService.transfer(fromId, toId, amount);
            response.put("success", true);
            response.put("message", "转账成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 查询员工信息
     * GET /api/employees/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        Employee employee = employeeService.getEmployee(id);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employee);
    }
}
