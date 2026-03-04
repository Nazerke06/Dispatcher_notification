-- =================================
-- PRODUCTS
-- =================================
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE transactions;
TRUNCATE TABLE fee;
TRUNCATE TABLE agent_merchants;
TRUNCATE TABLE merchant_products;
TRUNCATE TABLE agents;
TRUNCATE TABLE merchants;
TRUNCATE TABLE users;
TRUNCATE TABLE products;

SET FOREIGN_KEY_CHECKS = 1;


INSERT INTO products (id, code, name, category)
VALUES
    (UUID(), 'P001', 'Mobile Topup', 'TELCO'),
    (UUID(), 'P002', 'Internet Payment', 'TELCO'),
    (UUID(), 'P003', 'Utility Payment', 'UTILITY'),
    (UUID(), 'P004', 'TV Subscription', 'TELCO'),
    (UUID(), 'P005', 'Insurance', 'FINANCE'),
    (UUID(), 'P006', 'Loan Repayment', 'FINANCE'),
    (UUID(), 'P007', 'Taxi Service', 'TRANSPORT'),
    (UUID(), 'P008', 'Gaming', 'ENTERTAINMENT');

-- =================================
-- USERS
-- =================================
INSERT INTO users (id, external_id, email, full_name, country)
SELECT UUID(),
       CONCAT('U-', seq),
       CONCAT('user', seq, '@mail.com'),
       CONCAT('User ', seq),
       'KZ'
FROM (
         SELECT 1 seq UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
         UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10
         UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15
         UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20
     ) t;

-- =================================
-- MERCHANTS
-- =================================
INSERT INTO merchants (id, code, name, mcc, country, city)
VALUES
    (UUID(), 'M001', 'Shop A', '5411', 'KZ', 'Almaty'),
    (UUID(), 'M002', 'Shop B', '5999', 'KZ', 'Astana'),
    (UUID(), 'M003', 'Shop C', '5812', 'KZ', 'Shymkent'),
    (UUID(), 'M004', 'Shop D', '4900', 'KZ', 'Aktau'),
    (UUID(), 'M005', 'Shop E', '5732', 'KZ', 'Taraz');

-- =================================
-- AGENTS
-- =================================
INSERT INTO agents (id, code, name, type, country)
VALUES
    (UUID(), 'A01', 'Mobile App', 'APP', 'KZ'),
    (UUID(), 'A02', 'POS Partner', 'POS', 'KZ'),
    (UUID(), 'A03', 'Web Portal', 'WEB', 'KZ');

-- =================================
-- MERCHANT_PRODUCTS (M:N)
-- =================================
INSERT INTO merchant_products (merchant_id, product_id)
SELECT m.id, p.id
FROM merchants m
         JOIN products p;

-- =================================
-- AGENT_MERCHANTS (M:N)
-- =================================
INSERT INTO agent_merchants (agent_id, merchant_id)
SELECT a.id, m.id
FROM agents a
         JOIN merchants m;

-- =================================
-- FEE RULES
-- =================================
-- Создаем ~100 тарифов
INSERT INTO fee (id, merchant_id, agent_id, product_id, currency, calc_type, rate, fixed_amount, min_amount, max_amount)
SELECT UUID(),
       m.id,
       a.id,
       p.id,
       'KZT',
       CASE WHEN RAND() < 0.7 THEN 'PERCENT' ELSE 'FIXED' END,
       CASE WHEN RAND() < 0.7 THEN ROUND(RAND()*0.05,4) ELSE NULL END,
       CASE WHEN RAND() >= 0.7 THEN ROUND(RAND()*500+50,2) ELSE NULL END,
       ROUND(50+RAND()*100,2),
       ROUND(400+RAND()*100,2)
FROM merchants m
         CROSS JOIN agents a
         CROSS JOIN products p
    LIMIT 100;

-- =================================
-- TRANSACTIONS
-- =================================
-- Создаем 200 транзакций, 10 из них без fee
SET @row := 0;
INSERT INTO transactions (id, tx_id, buyer_user_id, merchant_id, agent_id, product_id,
                          amount, currency, status, success, result_code)
SELECT UUID(),
       CONCAT('TX-20260304-', LPAD(@row:=@row+1, 6, '0')),
       (SELECT id FROM users ORDER BY RAND() LIMIT 1),
       (SELECT id FROM merchants ORDER BY RAND() LIMIT 1),
       (SELECT id FROM agents ORDER BY RAND() LIMIT 1),
       (SELECT id FROM products ORDER BY RAND() LIMIT 1),
       ROUND(RAND()*10000+100,2),
       'KZT',
       CASE WHEN RAND() < 0.7 THEN 'SUCCESS' ELSE 'FAILED' END,
       CASE WHEN RAND() < 0.7 THEN TRUE ELSE FALSE END,
       CASE WHEN RAND() < 0.7 THEN 'APPROVED' ELSE 'DECLINED' END
FROM information_schema.columns
LIMIT 200;

-- Удаляем 10 fee для проверки NO_FEE_RULE
DELETE FROM fee
WHERE id IN (
    SELECT id FROM (
                       SELECT id FROM fee ORDER BY RAND() LIMIT 10
                   ) tmp
);