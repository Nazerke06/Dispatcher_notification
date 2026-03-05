# Dispatcher Notification - T3 Reports

## Quick start

1. Start database:
```bash
docker compose up -d db
```

2. Check DB is healthy:
```bash
docker compose ps
```

3. Start application (from IntelliJ run config or from terminal).
At startup Flyway migrations are applied automatically (`V1..V4`) and seed data is loaded.

## DB connection used by app

- URL: `jdbc:mariadb://localhost:3306/transactiondb?useSSL=false`
- User: `app`
- Password: `app123`

## Reports API

Base endpoint:
`GET /api/reports/transactions`

If deployed as WAR on Tomcat with app name `Dispatcher_notification`, use:
`http://localhost:8080/Dispatcher_notification/api/reports/transactions`

## Curl examples

1. Default first page:
```bash
curl "http://localhost:8080/Dispatcher_notification/api/reports/transactions?page=0&size=20"
```

2. Date range + success:
```bash
curl "http://localhost:8080/Dispatcher_notification/api/reports/transactions?from=2026-03-01T00:00:00Z&to=2026-03-31T23:59:59Z&success=true"
```

3. Merchant + product:
```bash
curl "http://localhost:8080/Dispatcher_notification/api/reports/transactions?merchantCode=M001&productCode=P003"
```

4. Filter by statuses and result codes:
```bash
curl "http://localhost:8080/Dispatcher_notification/api/reports/transactions?status=SUCCESS,FAILED&resultCode=APPROVED,DECLINED"
```

5. Commission range:
```bash
curl "http://localhost:8080/Dispatcher_notification/api/reports/transactions?minCommission=50&maxCommission=500"
```

6. Transactions without fee rule:
```bash
curl "http://localhost:8080/Dispatcher_notification/api/reports/transactions?hasFeeRule=false"
```
