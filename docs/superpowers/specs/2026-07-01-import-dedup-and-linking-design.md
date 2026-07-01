# 导入去重、手工创建检查与跨渠道关联设计

**日期：** 2026-07-01
**状态：** 已审核

## 目标

在现有 `DedupService` 基础上，完善以下场景：
1. 手工新增流水时检测重复，用户可确认后强制创建
2. 修复草稿提交时 `recordRole` 等字段丢失的问题
3. 跨渠道 TRACE 记录自动关联到 PRIMARY 记录
4. 被去重误标的记录可取消标记

## 实体变更

### TransactionRecord

新增字段：

| 字段 | 类型 | 说明 |
|---|---|---|
| `duplicateConfirmed` | `Boolean` | 设置 duplicateOf 后再设为 true，后续去重扫描跳过该记录 |

现有相关字段回顾：

| 字段 | 类型 | 说明 |
|---|---|---|
| `duplicateOf` | `Long` | 被判定为重复时指向保留记录 ID |
| `targetPrimaryRecordId` | `Long` | TRACE 记录指向其关联的 PRIMARY 记录 ID |
| `recordRole` | `RecordRoleEnum` | PRIMARY（实际资金）/ TRACE（辅助溯源） |
| `fundSource` | `String` | 资金来源描述 |
| `fundSourceAccountId` | `Long` | 资金来源账户 ID |

### CreateTransactionRecordDto

新增字段：

| 字段 | 类型 | 说明 |
|---|---|---|
| `confirmed` | `Boolean` | 当系统检测到重复时设为 true 确认是独立流水 |

## 接口设计

### 1. 手工新增流水去重检查（修改现有接口）

**`POST /api/finance/transactions`** — 行为变更

请求体中新增可选字段 `confirmed`。

当 `confirmed != true` 且系统检测到重复记录时 → 返回 400，附带匹配到的重复记录 ID：

```json
{
  "code": 400,
  "message": "检测到重复的流水：金额/time/商户 与记录 #101 匹配，如需确认请设置 confirmed=true",
  "data": null
}
```

当 `confirmed = true` 时 → 跳过检查，直接创建。

**`POST /api/finance/transactions/batch`** — 行为变更

任一记录检测到重复且 `confirmed != true` → 整个 batch 回滚，400 指明具体是哪一条。

### 2. 修复 recordRole 丢失

纯内部修复，无接口变更。`DraftCommitService.convert()` 保留以下字段：

- `recordRole`
- `fundSource`
- `fundSourceAccountId`
- `targetPrimaryRecordId`
- `merchant`
- `detail`

### 3. TRACE → PRIMARY 自动关联

**`POST /api/finance/accounts/{id}/link`**

请求体：

```json
{
  "timeToleranceMinutes": 5,
  "matchMerchant": true
}
```

逻辑：
1. 扫描账户下所有 `recordRole = TRACE` 且 `targetPrimaryRecordId IS NULL` 的记录
2. 按金额 + 时间窗口 + 方向（必选）+ 商户（可选）匹配 PRIMARY 记录
3. 匹配成功 → 设置 `targetPrimaryRecordId`
4. 无匹配 → 保持原样

响应：

```json
{
  "code": 200,
  "data": {
    "accountId": 1,
    "totalTraceRecords": 50,
    "linkedCount": 20,
    "unlinkedCount": 30,
    "details": [
      {
        "traceRecordId": 101,
        "linkedPrimaryRecordId": 201,
        "matchedBy": ["amount", "transactionTime", "directionType", "merchant"]
      }
    ]
  }
}
```

### 4. 取消去重标记

**`POST /api/finance/transactions/{id}/unmark-duplicate`**

将 `duplicateOf` 置为 null。如同时设置 `duplicateConfirmed = true`，后续全量扫描跳过该记录。

```json
{
  "duplicateConfirmed": true
}
```

## 新增/修改文件清单

### 新增文件

| 文件 | 说明 |
|---|---|
| `service/LinkingService.java` | 跨渠道关联服务 |
| `dto/link/LinkConfig.java` | 关联匹配配置 |
| `dto/link/LinkReport.java` | 关联结果报告 |

### 修改文件

| 文件 | 变更 |
|---|---|
| `entity/TransactionRecord.java` | 新增 `duplicateConfirmed` 字段 |
| `dto/transactions/CreateTransactionRecordDto.java` | 新增 `confirmed` 字段 |
| `service/TransactionService.java` | `doCreate()` 增加去重检查 |
| `service/DraftCommitService.java` | `convert()` 保留 recordRole 等字段 |
| `controler/AccountController.java` | 注入 `LinkingService`，新增 link 端点 |
| `controler/TransactionController.java` | 新增 unmark-duplicate 端点 |
| `DedupService.java` | 全量扫描时跳过 `duplicateConfirmed=true` 的记录 |

## 测试覆盖

| 测试 | 说明 |
|---|---|
| 手工创建时检测重复（confirmed=false）→ 400 | |
| 手工创建时检测重复（confirmed=true）→ 跳过检查 | |
| 批量创建时检测重复 → 回滚 + 400 | |
| DraftCommitService.convert() 保留 recordRole | |
| LinkingService 匹配 TRACE → PRIMARY | |
| LinkingService 不同金额不匹配 | |
| LinkingService 超过时间窗口不匹配 | |
| unmark-duplicate 清除 duplicateOf | |
| DedupService 跳过 duplicateConfirmed=true 的记录 | |
