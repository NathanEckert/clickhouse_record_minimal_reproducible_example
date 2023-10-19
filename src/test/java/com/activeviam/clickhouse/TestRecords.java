/*
 * (C) ActiveViam 2023
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of ActiveViam. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */

package com.activeviam.clickhouse;

import static org.assertj.core.api.Assertions.assertThat;

import com.clickhouse.client.ClickHouseException;
import com.clickhouse.client.ClickHouseResponse;
import com.clickhouse.data.ClickHouseRecord;
import com.clickhouse.data.ClickHouseVersion;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class TestRecords {
	@Container
	protected final static ClickHouseContainer CLICKHOUSE_SERVER = new ClickHouseContainer(
			ClickHouseVersion.of("23.3.13.6"));

	private static ClickHouseConfiguration CONFIG;

	@BeforeAll
	static void setUp() {
		final int port = CLICKHOUSE_SERVER.getFirstMappedPort();
		CONFIG = new ClickHouseConfiguration(
				CLICKHOUSE_SERVER.getHost(),
				port,
				null);
		ClickHouseContainer.populateDatabase(CONFIG);
	}

	@Test
	void testRecord() {
		String sql = "SELECT * FROM test.products";

		final Iterable<ClickHouseRecord> records;

		try (final ClickHouseResponse response =
				CONFIG.getRequest().query(sql).executeAndWait()) {
			records = response.records();
		} catch (ClickHouseException e) {
			throw new RuntimeException(e);
		}

		final List<ClickHouseRecord> out = StreamSupport.stream(records.spliterator(), false).collect(Collectors.toList());
		System.out.println("testRecord: " + out);
		assertThat(out).hasSize(9);
	}

	@Test
	void testStream() {
		String sql = "SELECT * FROM test.products";

		final List<ClickHouseRecord> out;

		try (final ClickHouseResponse response =
				CONFIG.getRequest().query(sql).executeAndWait()) {
			out = response.stream().collect(Collectors.toList());
		} catch (ClickHouseException e) {
			throw new RuntimeException(e);
		}
		System.out.println("testStream: " + out);
		assertThat(out).hasSize(9);
	}

	@AfterAll
	static void tearDown() {
		CONFIG.close();
	}
}
