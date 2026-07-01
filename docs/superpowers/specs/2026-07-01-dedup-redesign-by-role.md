# 去重重构：按记录角色分层去重

**日期：** 2026-07-01
**状态：** 草稿

## 动机

当前去重系统有两个问题：
1. `duplicateOf` + `duplicateConfirmed` 两个状态叠加，逻辑复杂
2. 去重没有区分记录角色（PRIMARY/TRACE/手工），一刀切

## 核心规则

| | PRIMARY | TRACE | 手工记录 |
|---|---|---|---|
| 去重范围 | 同一账户下所有 PRIMARY | 同一账户 + 同一渠道 | 不自动去重 |
| 匹配规则 | 金额+时间+方向+商户(可选) | 金额+时间(精确)+方向+商户+详情 | N/A |
| 发现重复时 | 标记 `duplicateOf` | 直接跳过/拒绝，不标记 | N/A |
| 手工创建时检查 | `confirmed=false` → 400 | 不检查 | 不检查 |

## 字段变更

### TransactionRecord

| 字段 | 变更 |
|---|---|
| `duplicateOf` | 保留，仅对 PRIMARY 记录设置 |
| `duplicateConfirmed` | **移除** |

## 接口变更

### 1. `POST /api/finance/accounts/{id}/deduplicate`

**改造：** 只扫描 `recordRole = PRIMARY` 的记录，TRACE 和手工记录不参与。

请求/响应不变（复用 `DedupConfig` / `DedupReport`）。

### 2. `POST /api/finance/accounts/{id}/cleanup`（新增）

两阶段确认清理：

**阶段 1 — 预览：**

```json
POST /api/finance/accounts/{id}/cleanup?preview=true
{
  "timeToleranceMinutes": 5,
  "matchMerchant": true
}

→ 返回 DedupReport（只扫描不标记）
```

**阶段 2 — 执行：**

```json
POST /api/finance/accounts/{id}/cleanup
{
  "timeToleranceMinutes": 5,
  "matchMerchant": true,
  "duplicateRecordIds": [101, 102, 103]
}

→ 将指定记录软删除（isDeleted = true），返回清理报告
```

### 3. `POST /api/finance/transactions/{id}/unmark-duplicate`

保持不变（仅 PRIMARY 记录有效，TRACE 不会出现 `duplicateOf`）。

## TRACE 去重实现

TRACE 去重在导入时执行（`DraftCommitService` / 导入流程），逻辑：

1. 加载该账户下同渠道的现有 TRACE 记录（通过 `account.channel.code` 判断渠道）
2. 严格匹配条件（全部必须一致）：
   - `amount`（金额相等）
   - `transactionTime`（精确到同一分钟，|t1 - t2| < 60 秒）
   - `directionType`（方向一致）
   - `merchant`（商户一致）
   - `detail`（详情一致）
3. 发现匹配 → 跳过（log.warn），不创建该 TRACE 记录
4. 不设 `duplicateOf`，不持久化任何标记

渠道通过 `account.channel.code` 判断。

## 受影响文件

### 修改

| 文件 | 变更 |
|---|---|
| `entity/TransactionRecord.java` | 移除 `duplicateConfirmed` |
| `service/DedupService.java` | `deduplicate()` 只扫描 PRIMARY；`findDuplicate()` 按角色分流 |
| `service/TransactionService.java` | `doCreate()` 只对 PRIMARY 检查重复 |
| `service/DraftCommitService.java` | `commitBatch()` 增加 TRACE 渠道内去重 |
| `controler/AccountController.java` | 新增 cleanup 端点 |
| `controler/TransactionController.java` | unmark 端点保持不变 |

### 删除

| 文件 | 说明 |
|---|---|
| `dto/link/` 相关内容 | LinkingService 保持不动，但删除与 `duplicateConfirmed` 相关的过滤逻辑 |

### 测试

所有涉及 `duplicateConfirmed` 的测试需要重写或移除。
