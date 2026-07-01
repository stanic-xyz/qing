# 账户账单去重设计

**日期：** 2026-07-01
**状态：** 已审核

## 目标

为 `qing-service-llm` 增加一个全账户扫描去重的接口，按可配置规则找出同一账户下的重复交易流水，标记并返回去重报告。

## 实体变更

### TransactionRecord

新增一个字段用于去重标记：

| 字段 | 类型 | 说明 |
|---|---|---|
| `duplicateOf` | `Long` | 若该记录被判定为重复，指向被保留的那条流水 ID；保留的记录该字段为 null |

## 接口设计

### `POST /api/finance/accounts/{id}/deduplicate`

**功能：** 对指定账户执行全量扫描去重，标记重复记录并返回报告。

**请求参数（JSON Body）：**

```json
{
  "timeToleranceMinutes": 5,
  "matchMerchant": true,
  "matchDetail": false
}
```

| 参数 | 类型 | 默认值 | 说明 |
|---|---|---|---|
| `timeToleranceMinutes` | int | 5 | 交易时间容忍分钟数，两条记录时间差 <= N 分钟即视为时间匹配 |
| `matchMerchant` | boolean | true | 是否匹配商户字段 |
| `matchDetail` | boolean | false | 是否匹配详情字段 |

**响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "accountId": 1,
    "totalRecords": 500,
    "duplicateGroups": 10,
    "markedCount": 12,
    "keptCount": 10,
    "details": [
      {
        "keptRecordId": 100,
        "duplicateRecordIds": [101, 102],
        "matchedBy": ["amount", "transactionTime", "directionType", "merchant"]
      }
    ]
  }
}
```

## 匹配规则

1. **必选匹配字段：** `amount`（金额相等）、`directionType`（方向一致）、`transactionTime`（在容忍窗口内）
2. **可选匹配字段：** `merchant`（商户一致）、`detail`（详情一致）
3. **分组规则：** 必选 + 可选字段完全一致的一组记录视为同一笔交易
4. **保留下规则：** 每组中按 `createdAt ASC` 保留最早的那条，其余标记 `duplicateOf`
5. **参与范围：** 仅扫描 `isDeleted = false` 且 `duplicateOf IS NULL` 的记录（已处理的不再重复扫描）

## 新增文件

| 文件 | 说明 |
|---|---|
| `service/DedupService.java` | 去重逻辑，仅依赖 Repository 层 |
| `dto/dedup/DedupConfig.java` | 可配置匹配参数 |
| `dto/dedup/DedupReport.java` | 去重报告（含明细） |

## 修改文件

| 文件 | 变更 |
|---|---|
| `entity/TransactionRecord.java` | 增加 `duplicateOf` 字段 |
| `controler/AccountController.java` | 实现已有 `deduplicate` 端点，注入 `DedupService` |

## 边界情况处理

| 场景 | 处理方式 |
|---|---|
| 账户不存在 | 返回 404 |
| 无重复记录 | 返回空报告，`duplicateGroups = 0` |
| 某组超过 100 条 | `markedCount` 计入，但 `details` 只列出前 99 条重复记录，多余的在 `hasMore` 中标记 |
| 软删除记录 | 不参与扫描 |
| 已标记的记录 | 已设置 `duplicateOf` 的记录不参与扫描 |

## 不涉及变更

- 不修改数据库 Schema（`duplicateOf` 的 JPA 字段会自动映射）
- 不修改现有导入/去重逻辑
- 不在 Repository 层新增方法（复用已有 `findAllByAccount`）
