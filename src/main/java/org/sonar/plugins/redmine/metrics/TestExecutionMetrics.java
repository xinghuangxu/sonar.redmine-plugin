/*
 * SonarQube Redmine Plugin
 * Copyright (C) 2013 Patroklos PAPAPETROU and Christian Schulz
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.redmine.metrics;

import java.util.List;

import org.sonar.api.measures.AverageFormula;
import org.sonar.api.measures.MeanAggregationFormula;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;
import org.sonar.api.measures.SumChildValuesFormula;

import com.google.common.collect.ImmutableList;

public class TestExecutionMetrics implements Metrics {

	public static String DOMAIN_TESTS = "QA Tests";

	public static final String TESTS_KEY = "qa_tests";
	public static final Metric TESTS = new Metric.Builder(TESTS_KEY,
			"QA tests", Metric.ValueType.INT)
			.setDescription("Number of QA tests")
			.setDirection(Metric.DIRECTION_WORST).setQualitative(false)
			.setFormula(new SumChildValuesFormula(false))
			.setDomain(DOMAIN_TESTS).create();

	public static final String TEST_EXECUTION_TIME_KEY = "qa_test_execution_time";
	public static final Metric TEST_EXECUTION_TIME = new Metric.Builder(
			TEST_EXECUTION_TIME_KEY, "QA tests duration",
			Metric.ValueType.MILLISEC)
			.setDescription("Execution duration of QA tests")
			.setDirection(Metric.DIRECTION_WORST).setQualitative(false)
			.setFormula(new SumChildValuesFormula(false))
			.setDomain(DOMAIN_TESTS).create();

	public static final String TEST_ERRORS_KEY = "qa_test_errors";
	public static final Metric TEST_ERRORS = new Metric.Builder(
			TEST_ERRORS_KEY, "QA test errors", Metric.ValueType.INT)
			.setDescription("Number of QA test errors")
			.setDirection(Metric.DIRECTION_WORST).setQualitative(true)
			.setFormula(new SumChildValuesFormula(false))
			.setDomain(DOMAIN_TESTS).setBestValue(0.0)
			.setOptimizedBestValue(true).create();

	public static final String SKIPPED_TESTS_KEY = "qa_skipped_tests";
	public static final Metric SKIPPED_TESTS = new Metric.Builder(
			SKIPPED_TESTS_KEY, "Skipped QA tests", Metric.ValueType.INT)
			.setDescription("Number of skipped QA tests")
			.setDirection(Metric.DIRECTION_WORST).setQualitative(true)
			.setFormula(new SumChildValuesFormula(false))
			.setDomain(DOMAIN_TESTS).setBestValue(0.0)
			.setOptimizedBestValue(true).create();

	public static final String TEST_FAILURES_KEY = "qa_test_failures";
	public static final Metric TEST_FAILURES = new Metric.Builder(
			TEST_FAILURES_KEY, "QA test failures", Metric.ValueType.INT)
			.setDescription("Number of QA test failures")
			.setDirection(Metric.DIRECTION_WORST).setQualitative(true)
			.setFormula(new SumChildValuesFormula(false))
			.setDomain(DOMAIN_TESTS).setBestValue(0.0)
			.setOptimizedBestValue(true).create();

	public static final String TEST_SUCCESS_DENSITY_KEY = "qa_test_success_density";
	public static final Metric TEST_SUCCESS_DENSITY = new Metric.Builder(
			TEST_SUCCESS_DENSITY_KEY, "QA test success (%)",
			Metric.ValueType.PERCENT)
			.setDescription("Density of successful QA tests")
			.setDirection(Metric.DIRECTION_BETTER).setQualitative(true)
			.setFormula(new MeanAggregationFormula(false))
			.setDomain(DOMAIN_TESTS).setWorstValue(0.0).setBestValue(100.0)
			.setOptimizedBestValue(true).create();

	public static final String TEST_DATA_KEY = "qa_test_data";
	public static final Metric TEST_DATA = new Metric.Builder(TEST_DATA_KEY,
			"QA tests details", Metric.ValueType.DATA)
			.setDescription("QA tests details")
			.setDirection(Metric.DIRECTION_WORST).setDomain(DOMAIN_TESTS)
			.create();
	
	
	public static final String TEST_DEFINED_KEY="qa_test_defined";
	public static final Metric TEST_DEFINED = new Metric.Builder(TEST_DEFINED_KEY,
			"QA tests defined", Metric.ValueType.INT)
			.setDescription("Number of QA tests defined")
			.setDirection(Metric.DIRECTION_WORST).setQualitative(false)
			.setFormula(new SumChildValuesFormula(false))
			.setDomain(DOMAIN_TESTS).create();
	
	public static final String TEST_EXECUTED_KEY="qa_test_executed";
	public static final Metric TEST_EXECUTED = new Metric.Builder(TEST_EXECUTED_KEY,
			"QA tests executed", Metric.ValueType.INT)
			.setDescription("Number of QA tests executed")
			.setDirection(Metric.DIRECTION_WORST).setQualitative(false)
			.setFormula(new SumChildValuesFormula(false))
			.setDomain(DOMAIN_TESTS).create();
	
	public static final String TEST_CLOSED_KEY="qa_test_closed";
	public static final Metric TEST_CLOSED = new Metric.Builder(TEST_CLOSED_KEY,
			"QA tests closed", Metric.ValueType.INT)
			.setDescription("Number of QA tests closed")
			.setDirection(Metric.DIRECTION_WORST).setQualitative(false)
			.setFormula(new SumChildValuesFormula(false))
			.setDomain(DOMAIN_TESTS).create();
	
	
	public static final String TEST_CLOSED_DENSITY_KEY = "qa_test_closed_density";
	public static final Metric TEST_CLOSED_DENSITY = new Metric.Builder(
			TEST_CLOSED_DENSITY_KEY, "QA test closed (%)",
			Metric.ValueType.PERCENT)
			.setDescription("Density of closed QA tests")
			.setDirection(Metric.DIRECTION_WORST).setQualitative(true)
			.setFormula(new MeanAggregationFormula(false))
			.setDomain(DOMAIN_TESTS).setWorstValue(0.0).setBestValue(100.0)
			.setOptimizedBestValue(true).create();
	
	
	public static final String TEST_REMAINING_KEY="qa_test_remaining";
	public static final Metric TEST_REMAINING = new Metric.Builder(TEST_REMAINING_KEY,
			"QA tests remaining", Metric.ValueType.INT)
			.setDescription("Number of QA tests remain")
			.setDirection(Metric.DIRECTION_WORST).setQualitative(false)
			.setFormula(new SumChildValuesFormula(false))
			.setDomain(DOMAIN_TESTS).create();
	
	public static final String TEST_BLOCKED_KEY="qa_test_blocked";
	public static final Metric TEST_BLOCKED = new Metric.Builder(TEST_BLOCKED_KEY,
			"QA tests blocked", Metric.ValueType.INT)
			.setDescription("Number of QA tests blocked")
			.setDirection(Metric.DIRECTION_WORST).setQualitative(false)
			.setFormula(new SumChildValuesFormula(false))
			.setDomain(DOMAIN_TESTS).create();
	
	
	@Override
	public List<Metric> getMetrics() {
		return ImmutableList.of(TESTS, TEST_EXECUTION_TIME,TEST_ERRORS,SKIPPED_TESTS,TEST_FAILURES,TEST_SUCCESS_DENSITY,TEST_DATA
				,TEST_DEFINED,TEST_EXECUTED,TEST_CLOSED,TEST_CLOSED_DENSITY,TEST_REMAINING,TEST_BLOCKED);
	}

}
