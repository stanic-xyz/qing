package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.entity.UploadFileRecord;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.repository.UploadFileRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance/uploads")
@Slf4j
public class UploadRecordController {

    @Autowired
    private UploadFileRecordRepository uploadFileRepo;

    @Autowired
    private TransactionRecordRepository transactionRepo;

    @GetMapping
    public Result<Page<UploadFileRecord>> getUploadRecords(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<UploadFileRecord> records = uploadFileRepo.findAll(pageRequest);
        return Result.success(records);
    }

    @GetMapping("/{id}/transactions")
    public Result<List<TransactionRecord>> getTransactionsByUploadId(@PathVariable("id") Long id) {
        UploadFileRecord record = uploadFileRepo.findById(id).orElse(null);
        if (record == null) {
            return Result.error(404, "导入记录不存在");
        }

        List<TransactionRecord> transactions = transactionRepo.findByUploadId(String.valueOf(record.getId()));

        if (transactions.isEmpty() && record.getFileName() != null) {
            // 回退到用文件名查找 (兼容旧数据)
            transactions = transactionRepo.findAll((root, query, cb) -> cb.equal(root.get("sourceFile"), record.getFileName()));
        }

        return Result.success(transactions);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteUploadRecord(@PathVariable Long id, @RequestParam(defaultValue = "true") boolean softDelete) {
        UploadFileRecord record = uploadFileRepo.findById(id).orElse(null);
        if (record == null) {
            return Result.error(404, "导入记录不存在");
        }

        List<TransactionRecord> transactions = transactionRepo.findByUploadId(String.valueOf(record.getId()));

        if (softDelete) {
            transactions.forEach(t -> t.setIsDeleted(true));
            transactionRepo.saveAll(transactions);
            record.setStatus("DELETED");
        } else {
            transactionRepo.deleteAll(transactions);
            uploadFileRepo.delete(record);
            return Result.success(null);
        }

        uploadFileRepo.save(record);
        return Result.success(null);
    }
}
