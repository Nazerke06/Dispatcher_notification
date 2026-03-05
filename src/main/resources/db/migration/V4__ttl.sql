
CREATE OR REPLACE VIEW transaction_report AS
SELECT
    t.tx_id,
    t.created_at,
    t.finished_at,
    t.status,
    t.success,
    t.result_code,
    t.amount,
    t.currency,
    p.code AS product_code,
    p.name AS product_name,
    p.category AS product_category,
    m.code AS merchant_code,
    m.name AS merchant_name,
    m.mcc AS merchant_mcc,
    m.country AS merchant_country,
    u.external_id AS buyer_external_id,
    u.email AS buyer_email,
    u.full_name AS buyer_full_name,
    a.code AS agent_code,
    a.name AS agent_name,
    a.type AS agent_type,
    f.calc_type AS fee_calc_type,
    f.currency AS fee_rule_currency,
    f.rate AS fee_rate,
    f.fixed_amount AS fee_fixed_amount,
    f.min_amount AS fee_min_amount,
    f.max_amount AS fee_max_amount,
    CASE
        WHEN f.id IS NULL THEN NULL
        WHEN f.calc_type = 'PERCENT' THEN
            LEAST(
                    GREATEST(t.amount * f.rate, COALESCE(f.min_amount, t.amount * f.rate)),
                    COALESCE(f.max_amount, t.amount * f.rate)
            )
        WHEN f.calc_type = 'FIXED' THEN
            LEAST(
                    GREATEST(f.fixed_amount, COALESCE(f.min_amount, f.fixed_amount)),
                    COALESCE(f.max_amount, f.fixed_amount)
            )
        ELSE NULL
        END AS commission_amount,
    CASE
        WHEN f.id IS NULL THEN 'NO_FEE_RULE'
        ELSE 'APPLIED'
        END AS commission_rule_status
FROM transactions t
         JOIN users u ON t.buyer_user_id = u.id
         JOIN merchants m ON t.merchant_id = m.id
         JOIN agents a ON t.agent_id = a.id
         JOIN products p ON t.product_id = p.id
         LEFT JOIN fee f ON f.merchant_id = m.id
    AND f.agent_id = a.id
    AND f.product_id = p.id
    AND f.currency = t.currency
    AND f.is_active = TRUE
    AND f.valid_from <= t.created_at
    AND (f.valid_to IS NULL OR f.valid_to >= t.created_at);






