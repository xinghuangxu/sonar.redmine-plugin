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
package org.sonar.plugins.redmine.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.DirectoryScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.redmine.exceptions.EmptyReportException;

public class RedmineReportSensor implements Sensor {

	protected String LanguageKey = "c++";
	protected Settings conf = null;
	protected Logger LOG = LoggerFactory.getLogger(RedmineReportSensor.class);

	public RedmineReportSensor(Settings conf) {
		this.conf = conf;
	}

	public boolean shouldExecuteOnProject(Project project) {
		return LanguageKey.equals(project.getLanguageKey());
	}

	/**
	 * {@inheritDoc}
	 */
	public void analyse(Project project, SensorContext context) {
		try {
			List<File> reports = getReports(conf, project.getFileSystem()
					.getBasedir().getPath(), reportPathKey(),
					defaultReportPath());
			for (File report : reports) {
				LOG.info("Processing report '{}'", report);
				try {
					processReport(project, context, report);
				} catch (EmptyReportException e) {
					LOG.warn(
							"The report '{}' seems to be empty, ignoring.",
							report);
				}
			}

			if (reports.isEmpty()) {
				handleNoReportsCase(context);
			}
		} catch (Exception e) {
			String msg = new StringBuilder()
					.append("Cannot feed the data into sonar, details: '")
					.append(e).append("'").toString();
			throw new SonarException(msg, e);
		}
	}

	protected List<File> getReports(Settings conf, String baseDirPath,
			String reportPathPropertyKey, String defaultReportPath) {
		String reportPath = conf.getString(reportPathPropertyKey);
		if (reportPath == null) {
			reportPath = defaultReportPath;
		}

		LOG.debug("Using pattern '{}' to find reports", reportPath);

		DirectoryScanner scanner = new DirectoryScanner();
		String[] includes = new String[1];
		includes[0] = reportPath;
		scanner.setIncludes(includes);
		scanner.setBasedir(new File(baseDirPath));
		scanner.scan();
		String[] relPaths = scanner.getIncludedFiles();

		List<File> reports = new ArrayList<File>();
		for (String relPath : relPaths) {
			reports.add(new File(baseDirPath, relPath));
		}

		return reports;
	}

	protected void processReport(Project project, SensorContext context,
			File report) throws Exception {
	}

	protected void handleNoReportsCase(SensorContext context) {
	}

	protected String reportPathKey() {
		return "";
	};

	protected String defaultReportPath() {
		return "";
	};
}
