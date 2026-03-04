-- =====================================
-- V1__schema_mariadb_full_reset.sql
-- =====================================

-- Отключаем проверку FK, чтобы можно было удалять таблицы в любом порядке
SET FOREIGN_KEY_CHECKS = 0;

-- ===============================
-- DROP ALL TABLES
-- ===============================
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS fee;
DROP TABLE IF EXISTS agent_merchants;
DROP TABLE IF EXISTS merchant_products;
DROP TABLE IF EXISTS agents;
DROP TABLE IF EXISTS merchants;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS products;

-- Включаем проверку FK обратно
SET FOREIGN_KEY_CHECKS = 1;

-- ===============================
-- PRODUCTS
-- ===============================
CREATE TABLE products (
                          id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                          code VARCHAR(100) UNIQUE NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          category VARCHAR(100) NOT NULL,
                          is_active BOOLEAN NOT NULL DEFAULT TRUE,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ===============================
-- USERS
-- ===============================
CREATE TABLE users (
                       id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                       external_id VARCHAR(100) UNIQUE,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       full_name VARCHAR(255) NOT NULL,
                       phone VARCHAR(50),
                       country VARCHAR(100),
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ===============================
-- MERCHANTS
-- ===============================
CREATE TABLE merchants (
                           id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                           code VARCHAR(100) UNIQUE NOT NULL,
                           name VARCHAR(255) NOT NULL,
                           mcc VARCHAR(20),
                           country VARCHAR(100),
                           city VARCHAR(100),
                           is_active BOOLEAN NOT NULL DEFAULT TRUE,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ===============================
-- AGENTS
-- ===============================
CREATE TABLE agents (
                        id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                        code VARCHAR(100) UNIQUE NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        type VARCHAR(50) NOT NULL,
                        country VARCHAR(100),
                        is_active BOOLEAN NOT NULL DEFAULT TRUE,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ===============================
-- MANY-TO-MANY RELATIONS
-- ===============================
CREATE TABLE merchant_products (
                                   merchant_id CHAR(36) NOT NULL,
                                   product_id CHAR(36) NOT NULL,
                                   PRIMARY KEY (merchant_id, product_id),
                                   FOREIGN KEY (merchant_id) REFERENCES merchants(id) ON DELETE CASCADE,
                                   FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE agent_merchants (
                                 agent_id CHAR(36) NOT NULL,
                                 merchant_id CHAR(36) NOT NULL,
                                 PRIMARY KEY (agent_id, merchant_id),
                                 FOREIGN KEY (agent_id) REFERENCES agents(id) ON DELETE CASCADE,
                                 FOREIGN KEY (merchant_id) REFERENCES merchants(id) ON DELETE CASCADE
);

-- ===============================
-- FEE
-- ===============================
CREATE TABLE fee (
                     id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                     merchant_id CHAR(36) NOT NULL,
                     agent_id CHAR(36) NOT NULL,
                     product_id CHAR(36) NOT NULL,
                     currency VARCHAR(10) NOT NULL,
                     calc_type VARCHAR(50) NOT NULL,
                     rate DECIMAL(10,4),
                     fixed_amount DECIMAL(18,2),
                     min_amount DECIMAL(18,2),
                     max_amount DECIMAL(18,2),
                     is_active BOOLEAN NOT NULL DEFAULT TRUE,
                     valid_from TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     valid_to TIMESTAMP NULL,
                     FOREIGN KEY (merchant_id) REFERENCES merchants(id),
                     FOREIGN KEY (agent_id) REFERENCES agents(id),
                     FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Индекс для быстрого поиска
ALTER TABLE fee ADD INDEX idx_fee_lookup (merchant_id, agent_id, product_id, currency);

-- ===============================
-- TRANSACTIONS
-- ===============================
CREATE TABLE transactions (
                              id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                              tx_id VARCHAR(100) UNIQUE NOT NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              finished_at TIMESTAMP NULL,
                              buyer_user_id CHAR(36) NOT NULL,
                              merchant_id CHAR(36) NOT NULL,
                              agent_id CHAR(36) NOT NULL,
                              product_id CHAR(36) NOT NULL,
                              amount DECIMAL(18,2) NOT NULL,
                              currency VARCHAR(10) NOT NULL,
                              status VARCHAR(50) NOT NULL,
                              result_code VARCHAR(50),
                              success BOOLEAN NOT NULL,
                              rrn VARCHAR(100),
                              stan VARCHAR(100),
                              payment_method VARCHAR(100),
                              FOREIGN KEY (buyer_user_id) REFERENCES users(id),
                              FOREIGN KEY (merchant_id) REFERENCES merchants(id),
                              FOREIGN KEY (agent_id) REFERENCES agents(id),
                              FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Индексы для ускорения поиска
ALTER TABLE transactions ADD INDEX idx_transactions_created_at (created_at);
ALTER TABLE transactions ADD INDEX idx_transactions_product (product_id);
ALTER TABLE transactions ADD INDEX idx_transactions_success (success);