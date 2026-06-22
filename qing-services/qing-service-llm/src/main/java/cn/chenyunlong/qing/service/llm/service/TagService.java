package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.entity.Tag;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.TagType;
import cn.chenyunlong.qing.service.llm.repository.TagRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Tag> listAll() {
        return tagRepository.findByIsDeletedFalse();
    }

    /**
     * 按 ID 加载未删除标签，不存在时抛出资源不存在异常。
     *
     * @param id 标签 ID
     * @return 标签实体
     */
    public Tag getById(Long id) {
        return tagRepository.findById(id)
                .filter(t -> !Boolean.TRUE.equals(t.getIsDeleted()))
                .orElseThrow(() -> new NotFoundException("标签不存在: " + id));
    }

    @Transactional
    public Tag create(String name, String color) {
        validateTagName(name);
        validateUniqueTagName(name, null);

        Tag tag = new Tag();
        tag.setName(name.trim());
        tag.setColor(color);
        tag.setType(TagType.USER);
        return tagRepository.save(tag);
    }

    @Transactional
    public Tag update(Long id, String name, String color) {
        Tag tag = getById(id);
        validateTagName(name);
        validateUniqueTagName(name, id);

        tag.setName(name.trim());
        if (color != null) {
            tag.setColor(color);
        }
        return tagRepository.save(tag);
    }

    @Transactional
    public void delete(Long id) {
        Tag tag = getById(id);
        tag.setIsDeleted(true);
        tagRepository.save(tag);
    }

    public Tag findOrCreate(String name) {
        return tagRepository.findByNameAndIsDeletedFalse(name)
                .orElseGet(() -> create(name, null));
    }

    public List<Tag> getTagsByTransactionId(Long transactionId) {
        TransactionRecord record = getTransactionRecordOrThrow(transactionId);
        return parseTagsFromJson(record.getTags());
    }

    @Transactional
    public void addTagToTransaction(Long transactionId, Long tagId) {
        TransactionRecord record = getTransactionRecordOrThrow(transactionId);
        Tag tag = getById(tagId);

        List<Tag> tags = getTagsByTransactionId(transactionId);
        if (tags.stream().noneMatch(t -> t.getId().equals(tagId))) {
            tags.add(tag);
            record.setTags(serializeTags(tags));
            transactionRecordRepository.save(record);
        }
    }

    @Transactional
    public void removeTagFromTransaction(Long transactionId, Long tagId) {
        TransactionRecord record = getTransactionRecordOrThrow(transactionId);
        List<Tag> tags = getTagsByTransactionId(transactionId);
        tags.removeIf(t -> t.getId().equals(tagId));
        record.setTags(serializeTags(tags));
        transactionRecordRepository.save(record);
    }

    public Map<String, Long> getTagStatistics() {
        List<Tag> allTags = tagRepository.findByIsDeletedFalse();
        List<TransactionRecord> allRecords = transactionRecordRepository.findAll();

        return allTags.stream()
                .collect(Collectors.toMap(
                        Tag::getName,
                        tag -> countTransactionsWithTag(allRecords, tag.getName())
                ));
    }

    private long countTransactionsWithTag(List<TransactionRecord> records, String tagName) {
        return records.stream()
                .filter(r -> r.getTags() != null && r.getTags().contains(tagName))
                .count();
    }

    /**
     * 校验标签名称参数。
     *
     * @param name 标签名称
     */
    private void validateTagName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("标签名称不能为空");
        }
    }

    /**
     * 校验标签名称唯一性，避免创建或更新为重复标签。
     *
     * @param name 标签名称
     * @param currentTagId 当前标签 ID，创建场景传 null
     */
    private void validateUniqueTagName(String name, Long currentTagId) {
        String normalizedName = name.trim();
        tagRepository.findByNameAndIsDeletedFalse(normalizedName)
                .filter(existing -> !existing.getId().equals(currentTagId))
                .ifPresent(existing -> {
                    throw new BusinessException("标签名称已存在: " + normalizedName);
                });
    }

    /**
     * 按 ID 加载交易记录，不存在时抛出资源不存在异常。
     *
     * @param transactionId 交易记录 ID
     * @return 交易记录实体
     */
    private TransactionRecord getTransactionRecordOrThrow(Long transactionId) {
        return transactionRecordRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("交易记录不存在: " + transactionId));
    }

    private List<Tag> parseTagsFromJson(String tagsJson) {
        if (tagsJson == null || tagsJson.isBlank()) {
            return new ArrayList<>();
        }
        try {
            List<String> tagNames = objectMapper.readValue(tagsJson, new TypeReference<List<String>>() {});
            List<Tag> tags = new ArrayList<>();
            for (String name : tagNames) {
                tagRepository.findByNameAndIsDeletedFalse(name)
                        .ifPresent(tags::add);
            }
            return tags;
        } catch (JsonProcessingException e) {
            log.warn("解析标签JSON失败: {}", tagsJson, e);
            return new ArrayList<>();
        }
    }

    private String serializeTags(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        try {
            List<String> names = tags.stream()
                    .map(Tag::getName)
                    .collect(Collectors.toList());
            return objectMapper.writeValueAsString(names);
        } catch (JsonProcessingException e) {
            log.warn("序列化标签失败", e);
            return null;
        }
    }
}
