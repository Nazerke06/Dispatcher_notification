-- ===============================
-- PRODUCTS
-- ===============================
CREATE TABLE products (
                          id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                          code VARCHAR(100) UNIQUE NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          category VARCHAR(100) NOT NULL,
                          is_active BOOLEAN NOT NULL DEFAULT true,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_code ON products(code);


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

CREATE INDEX idx_users_email ON users(email);


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
                           is_active BOOLEAN NOT NULL DEFAULT true,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_merchants_code ON merchants(code);


-- ===============================
-- AGENTS
-- ===============================
CREATE TABLE agents (
                        id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                        code VARCHAR(100) UNIQUE NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        type VARCHAR(50) NOT NULL,
                        country VARCHAR(100),
                        is_active BOOLEAN NOT NULL DEFAULT true,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_agents_code ON agents(code);


-- ===============================
-- MANY-TO-MANY
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

                     is_active BOOLEAN NOT NULL DEFAULT true,

                     valid_from TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     valid_to TIMESTAMP NULL,

                     FOREIGN KEY (merchant_id) REFERENCES merchants(id),
                     FOREIGN KEY (agent_id) REFERENCES agents(id),
                     FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE INDEX idx_fee_lookup
    ON fee(merchant_id, agent_id, product_id, currency);


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

CREATE INDEX idx_transactions_created_at ON transactions(created_at);
CREATE INDEX idx_transactions_merchant ON transactions(merchant_id);
CREATE INDEX idx_transactions_agent ON transactions(agent_id);
CREATE INDEX idx_transactions_product ON transactions(product_id);
CREATE INDEX idx_transactions_success ON transactions(success);