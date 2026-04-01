-- 此脚本用于向 finance_channel 表中初始化常见的支付渠道
-- 表中已存在 code 的记录将会被跳过（不会重复插入）

INSERT INTO finance_channel (code, name, status, is_enabled, version, is_deleted, created_by, created_at, updated_at)
VALUES
    ('ALIPAY', '支付宝', 'EFFECTIVE', true, 0, false, 'system_init', NOW(), NOW()),
    ('WECHAT', '微信支付', 'EFFECTIVE', true, 0, false, 'system_init', NOW(), NOW()),
    ('UNIONPAY', '云闪付', 'EFFECTIVE', true, 0, false, 'system_init', NOW(), NOW()),
    ('JINGDONG', '京东支付', 'EFFECTIVE', true, 0, false, 'system_init', NOW(), NOW()),
    ('MEITUAN', '美团支付', 'EFFECTIVE', true, 0, false, 'system_init', NOW(), NOW()),
    ('DOUYIN', '抖音支付', 'EFFECTIVE', true, 0, false, 'system_init', NOW(), NOW()),
    ('PAYPAL', 'PayPal', 'EFFECTIVE', true, 0, false, 'system_init', NOW(), NOW()),
    ('APPLE_PAY', 'Apple Pay', 'EFFECTIVE', true, 0, false, 'system_init', NOW(), NOW()),
    ('ALIPAY_HK', '支付宝 HK', 'EFFECTIVE', true, 0, false, 'system_init', NOW(), NOW()),
    ('WECHAT_HK', 'WeChat Pay HK', 'EFFECTIVE', true, 0, false, 'system_init', NOW(), NOW())
ON CONFLICT (code) DO NOTHING;
