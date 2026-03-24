package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;

import java.io.InputStream;
import java.util.List;

public interface FileParser {
    /**
     * 解析输入流，返回标准化交易记录列表
     *
     * @param inputStream      文件输入流
     * @param originalFilename 原始文件名（可用于判断格式）
     * @return 交易记录列表（未持久化，不包含ID）
     */
    List<TransactionRecord> parse(InputStream inputStream, String originalFilename) throws Exception;
}
