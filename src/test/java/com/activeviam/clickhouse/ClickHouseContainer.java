/*
 * (C) ActiveViam 2023
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of ActiveViam. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */

package com.activeviam.clickhouse;


import com.clickhouse.client.ClickHouseProtocol;
import com.clickhouse.data.ClickHouseVersion;
import java.time.Duration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.utility.DockerImageName;

public class ClickHouseContainer extends GenericContainer<ClickHouseContainer> {

	private static final String CLICKHOUSE_DOCKER_PREFIX = "clickhouse/clickhouse-server:";
	private static final String CLICKHOUSE_DOCKER_SUFFIX = "-alpine";


	public ClickHouseContainer(final ClickHouseVersion clickhouseVersion) {
		super(DockerImageName.parse(CLICKHOUSE_DOCKER_PREFIX + clickhouseVersion + CLICKHOUSE_DOCKER_SUFFIX));
		// Always use the same port as it will be remapped by container manager on a free port
		this.withExposedPorts(ClickHouseProtocol.HTTP.getDefaultPort())
				.waitingFor((new HttpWaitStrategy()).forStatusCode(200).forResponsePredicate("Ok."::equals))
				.withStartupTimeout(Duration.ofMinutes(2L));
	}

	public static void populateDatabase(ClickHouseConfiguration configuration) {
		String sql = "CREATE database test;";
		configuration.sendSqlQueryToNode(sql);
		sql = "CREATE TABLE\n"
				+ "  test.products\n"
				+ "(\n"
				+ "  `id` String NOT NULL,\n"
				+ "  `date` Date NOT NULL,\n"
				+ "  `unit_price` Float64 NOT NULL\n"
				+ ")\n"
				+ "ENGINE = MergeTree()\n"
				+ "ORDER BY (id, date)\n"
				+ ";";
		configuration.sendSqlQueryToNode(sql);
		sql = "INSERT INTO\n"
				+ "  test.products\n"
				+ "VALUES\n"
				+ "('p1', '2021-07-19', 10),\n"
				+ "('p1', '2021-07-20', 10),\n"
				+ "('p1', '2021-07-21', 10),\n"
				+ "('p2', '2021-07-19', 20),\n"
				+ "('p2', '2021-07-20', 21),\n"
				+ "('p2', '2021-07-21', 22),\n"
				+ "('p3', '2021-07-19', 40),\n"
				+ "('p3', '2021-07-20', 38),\n"
				+ "('p3', '2021-07-21', 42);";
		configuration.sendSqlQueryToNode(sql);
	}


}
