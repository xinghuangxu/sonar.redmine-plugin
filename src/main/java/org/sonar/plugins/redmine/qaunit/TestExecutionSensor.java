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
package org.sonar.plugins.redmine.qaunit;

import java.io.File;
import java.io.IOException;





import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.utils.ParsingUtils;
import org.sonar.api.utils.StaxParser;
import org.sonar.plugins.redmine.config.RedmineSettings;
import org.sonar.plugins.redmine.metrics.TestExecutionMetrics;
import org.sonar.plugins.redmine.utils.RedmineReportSensor;


public class TestExecutionSensor extends RedmineReportSensor {

	
	public static final String REPORT_PATH_KEY = "sonar.qa.reportPath";
	private static final String DEFAULT_REPORT_PATH = "qa-reports/*.xml";
	
	public TestExecutionSensor(Settings conf,RedmineSettings redmineSettings) {
		super(conf,redmineSettings);
	}
	

	@Override
	protected String reportPathKey() {
		return REPORT_PATH_KEY;
	}

	@Override
	protected String defaultReportPath() {
		return DEFAULT_REPORT_PATH;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void analyse(Project project, SensorContext context) {
		try{
			super.analyse(project, context);
		}catch(Exception ex){
			LOG.info("Test Execution Sensor: A Exception happend during the analysis."+ex.getMessage());
		}
		
	}

	@Override
	protected void processReport(final Project project,
			final SensorContext context, File report)
			throws java.io.IOException,
			javax.xml.transform.TransformerException,
			javax.xml.stream.XMLStreamException {
		parseReport(project, context, report);
	}

	@Override
	protected void handleNoReportsCase(SensorContext context) {
		context.saveMeasure(CoreMetrics.TESTS, 0.0);
	}

	private void parseReport(Project project, SensorContext context, File report)
			throws javax.xml.stream.XMLStreamException, IOException {
		LOG.info("Parsing report '{}'", report);

		TestSuiteParser parserHandler = new TestSuiteParser();
		StaxParser parser = new StaxParser(parserHandler, false);
		parser.parse(report);

		for (TestSuite fileReport : parserHandler.getParsedReports()) {
			String fileKey = fileReport.getKey();
			double testsCount = fileReport.getTests() - fileReport.getSkipped();

			try {
				org.sonar.api.resources.File resource = getTestFile(project,
						context, fileKey);
				saveTestMetrics(context, resource, fileReport, testsCount);
			} catch (org.sonar.api.utils.SonarException ex) {
				LOG.warn(
						"Cannot save test metrics for '{}', details: {}",
						fileKey, ex);
			}
		}
	}

	private void saveTestMetrics(SensorContext context,
			org.sonar.api.resources.File resource, TestSuite fileReport,
			double testsCount) {
		double runned=0;
		if(fileReport.getSkipped()==0){
			runned=1;
		}
		
		double failed=(double)fileReport.getFailures();
		double closed=0;
		double closedDensity=0;
		if(runned==1&&failed==0){
			closed=1;
			closedDensity=100;
		}
		double remaining=0;
		if(runned==0 || failed ==1)
			remaining=1;
		
		context.saveMeasure(resource, TestExecutionMetrics.TEST_DEFINED,
				1d);
		if(runned==1){
			context.saveMeasure(resource, TestExecutionMetrics.TEST_EXECUTED,
					runned);
		}
		if(closed==1){
			context.saveMeasure(resource, TestExecutionMetrics.TEST_CLOSED,
					closed);
		}
		final double passedTests = testsCount - fileReport.getFailures();
		if(runned==1){
			double percentage = passedTests * 100d / testsCount;

			context.saveMeasure(resource, TestExecutionMetrics.TEST_CLOSED_DENSITY,
					ParsingUtils.scaleValue(percentage));
		}
//		if(runned==1){
//			context.saveMeasure(resource, TestExecutionMetrics.TEST_CLOSED_DENSITY,
//					closedDensity);
//		}
		if(remaining==1){
			context.saveMeasure(resource, TestExecutionMetrics.TEST_REMAINING,
					remaining);
		}
		if(failed==1){
			context.saveMeasure(resource, TestExecutionMetrics.TEST_FAILURES,
					failed);
		}
		
		context.saveMeasure(resource, TestExecutionMetrics.TEST_BLOCKED,
				0d);
		
		context.saveMeasure(resource, TestExecutionMetrics.SKIPPED_TESTS,
				(double) fileReport.getSkipped());
		context.saveMeasure(resource, TestExecutionMetrics.TESTS, testsCount+fileReport.getSkipped());
		context.saveMeasure(resource, TestExecutionMetrics.TEST_ERRORS,
				(double) fileReport.getErrors());
//		context.saveMeasure(resource, TestExecutionMetrics.TEST_FAILURES,
//				(double) fileReport.getFailures());
		context.saveMeasure(resource, TestExecutionMetrics.TEST_EXECUTION_TIME,
				(double) fileReport.getTime());
		
//		final double passedTests = testsCount - fileReport.getErrors()
//				- fileReport.getFailures();
//		if (testsCount > 0) {
//			double percentage = passedTests * 100d / testsCount;
//
//			context.saveMeasure(resource, TestExecutionMetrics.TEST_SUCCESS_DENSITY,
//					ParsingUtils.scaleValue(percentage));
//		}
		context.saveMeasure(resource, new Measure(TestExecutionMetrics.TEST_DATA,
				fileReport.getDetails()));
	}

	private org.sonar.api.resources.File getTestFile(Project project,
			SensorContext context, String fileKey) {

		@SuppressWarnings("deprecation")
		org.sonar.api.resources.File resource = org.sonar.api.resources.File
				.fromIOFile(new File(fileKey), project.getFileSystem()
						.getTestDirs());
		if (context.getResource(resource) == null) {
			LOG.debug("Cannot find the source file for test '{}', creating a dummy one", fileKey);
	        resource = createVirtualFile(context, fileKey);   
		}
		else{
			LOG.debug("Assigning the test '{}' to resource '{}'", fileKey,
					resource.getKey());
		}

		return resource;
	}

	@SuppressWarnings("deprecation")
	private org.sonar.api.resources.File createVirtualFile(
			SensorContext context, String fileKey) {
		org.sonar.api.resources.File file = new org.sonar.api.resources.File(
				fileKey);
		file.setQualifier(Qualifiers.UNIT_TEST_FILE);
		context.saveSource(file,
				"<The sources could not be found. Consult the log file for details>");
		return file;
	}



	@Override
	public String toString() {
		return "QA Test sensor";
	}

}
