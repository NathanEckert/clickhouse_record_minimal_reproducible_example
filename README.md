# Minimal reproducible example: `ClickHouseResponse.records()`

`ClickHouseResponse.records()` returns no records when some are expected.

## Test

Run the test `TestRecords` via `mvn clean test`

`testRecord` fails and does not mimic the same behavior as `testStream`

Issue:

```
testRecord: []
testStream: [com.clickhouse.data.ClickHouseSimpleRecord@466d49f0, com.clickhouse.data.ClickHouseSimpleRecord@466d49f0, com.clickhouse.data.ClickHouseSimpleRecord@466d49f0, com.clickhouse.data.ClickHouseSimpleRecord@466d49f0, com.clickhouse.data.ClickHouseSimpleRecord@466d49f0, com.clickhouse.data.ClickHouseSimpleRecord@466d49f0, com.clickhouse.data.ClickHouseSimpleRecord@466d49f0, com.clickhouse.data.ClickHouseSimpleRecord@466d49f0, com.clickhouse.data.ClickHouseSimpleRecord@466d49f0]
```

## Configuration

```
Apache Maven 3.8.6 (84538c9988a25aec085021c365c560670ad80f63)
Maven home: /home/nec/.sdkman/candidates/maven/current
Java version: 11.0.20, vendor: Eclipse Adoptium, runtime: /home/nec/.sdkman/candidates/java/11.0.20-tem
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "6.2.0-34-generic", arch: "amd64", family: "unix"
```