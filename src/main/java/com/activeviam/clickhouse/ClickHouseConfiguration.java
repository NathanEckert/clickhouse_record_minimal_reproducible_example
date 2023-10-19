/*
 * (C) ActiveViam 2023
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of ActiveViam. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */

package com.activeviam.clickhouse;

import com.clickhouse.client.ClickHouseClient;
import com.clickhouse.client.ClickHouseClientBuilder;
import com.clickhouse.client.ClickHouseException;
import com.clickhouse.client.ClickHouseNode;
import com.clickhouse.client.ClickHouseNodeSelector;
import com.clickhouse.client.ClickHouseProtocol;
import com.clickhouse.client.ClickHouseRequest;
import com.clickhouse.client.ClickHouseResponse;
import com.clickhouse.client.config.ClickHouseClientOption;
import com.clickhouse.data.ClickHouseFormat;


public class ClickHouseConfiguration implements AutoCloseable {

	private final ClickHouseNode node;
	private final ClickHouseClient client;

	public ClickHouseConfiguration(String host, int port, String database) {

		final ClickHouseProtocol protocol = ClickHouseProtocol.HTTP;

		this.node = ClickHouseNode.builder()
				.host(host)
				.port(protocol, port)
				.database(database)
				.addOption(ClickHouseClientOption.SSL.getKey(), "false")
				.build();
		ClickHouseClientBuilder builder = ClickHouseClient.builder()
				.nodeSelector(ClickHouseNodeSelector.of(protocol));
		this.client = builder.build();
	}

	@Override
	public void close() {
		this.client.close();
	}

	@SuppressWarnings("unchecked")
	public <T extends ClickHouseRequest<T>> T getRequest() {
		return ((T) this.client.read(this.node)).format(ClickHouseFormat.RowBinaryWithNamesAndTypes);
	}

	public void sendSqlQueryToNode(final String sql) {

		ClickHouseRequest<? extends ClickHouseRequest<?>> query = getRequest().query(sql);

		try (ClickHouseResponse response = query.executeAndWait()) {
			// wait for OK response
		} catch (ClickHouseException e) {
			throw new RuntimeException(e);
		}
	}
}
