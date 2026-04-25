package cn.chenyunlong.qing.service.qmall.service;

import cn.chenyunlong.qing.service.qmall.entity.Employee;
import cn.chenyunlong.qing.service.qmall.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * 转账业务 - 演示事务回滚
     * 使用默认隔离级别（MySQL默认为 REPEATABLE_READ）
     */
    @Transactional(rollbackFor = Exception.class)
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        // 1. 查询转出账户
        Employee fromEmp = employeeRepository.findById(fromId)
                .orElseThrow(() -> new RuntimeException("转出账户不存在"));

        // 2. 查询转入账户
        Employee toEmp = employeeRepository.findById(toId)
                .orElseThrow(() -> new RuntimeException("转入账户不存在"));

        // 3. 检查余额是否充足
        if (fromEmp.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("余额不足，转账失败");
        }

        // 4. 执行转账
        fromEmp.setBalance(fromEmp.getBalance().subtract(amount));
        toEmp.setBalance(toEmp.getBalance().add(amount));

        employeeRepository.save(fromEmp);
        employeeRepository.save(toEmp);

        log.info("转账成功：{} 向 {} 转账 {} 元", fromEmp.getName(), toEmp.getName(), amount);

        // 模拟异常（用于测试事务回滚）
        // if (true) throw new RuntimeException("模拟转账异常，事务应该回滚");
    }

    /**
     * 查询员工余额 - 只读事务，设置隔离级别为 READ_COMMITTED
     *
     * @Transactional(readOnly = true) 会告诉 Spring 这是一个只读事务，可以进行一些性能优化[reference:1]
     */
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    /**
     * 高并发场景下使用 SERIALIZABLE 隔离级别
     * 此级别最高，但性能最低，慎用
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateBalanceWithSerializable(Long id, BigDecimal newBalance) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("员工不存在"));
        emp.setBalance(newBalance);
        employeeRepository.save(emp);
    }
}
