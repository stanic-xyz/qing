package cn.chenyunlong.qing.service.llm.service;

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

    public Tag getById(Long id) {
        return tagRepository.findById(id)
                .filter(t -> !Boolean.TRUE.equals(t.getIsDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("标签不存在: " + id));
    }

    @Transactional
    public Tag create(String name, String color) {
        if (tagRepository.existsByNameAndIsDeletedFalse(name)) {
            throw new IllegalArgumentException("标签已存在: " + name);
        }

        Tag tag = new Tag();
        tag.setName(name);
        tag.setColor(color);
        tag.setType(TagType.USER);
        return tagRepository.save(tag);
    }

    @Transactional
    public Tag update(Long id, String name, String color) {
        Tag tag = getById(id);

        if (!tag.getName().equals(name) && tagRepository.existsByNameAndIsDeletedFalse(name)) {
            throw new IllegalArgumentException("标签名称已存在: " + name);
        }

        tag.setName(name);
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
        TransactionRecord record = transactionRecordRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("交易记录不存在: " + transactionId));

        return parseTagsFromJson(record.getTags());
    }

    @Transactional
    public void addTagToTransaction(Long transactionId, Long tagId) {
        TransactionRecord record = transactionRecordRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("交易记录不存在: " + transactionId));

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
        TransactionRecord record = transactionRecordRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("交易记录不存在: " + transactionId));

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
