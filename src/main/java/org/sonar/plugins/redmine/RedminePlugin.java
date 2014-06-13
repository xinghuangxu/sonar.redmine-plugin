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
package org.sonar.plugins.redmine;

import com.google.common.collect.ImmutableList;

import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.PropertyType;
import org.sonar.api.SonarPlugin;
import org.sonar.plugins.redmine.qaunit.TestExecutionSensor;
import org.sonar.plugins.redmine.config.RedmineSettings;
import org.sonar.plugins.redmine.exceptions.RedmineGeneralException;
import org.sonar.plugins.redmine.exceptions.RedmineNotAuthorizedException;
import org.sonar.plugins.redmine.exceptions.RedmineNotFoundException;
import org.sonar.plugins.redmine.metrics.TestExecutionMetrics;
import org.sonar.plugins.redmine.reviews.RedmineIssueFactory;
import org.sonar.plugins.redmine.reviews.RedmineLinkFunction;
import org.sonar.plugins.redmine.reviews.RedmineWorkflowBuilder;
import org.sonar.plugins.redmine.ui.RedmineBcrefWidget;
import org.sonar.plugins.redmine.ui.RedmineSettingsPage;
import org.sonar.plugins.redmine.ui.RedmineWidget;
import org.sonar.plugins.redmine.ui.TestExecutionWidget;

import java.util.List;

import org.sonar.plugins.redmine.client.RedmineAdapter;

@Properties({
	    @Property(key =RedmineSettings.REF_NAME, name =  "Reference Name",defaultValue="", description = "Set Release or Boxcar name such as: Kingston, Misbehaving Drives, Rapid Construct Feature ...",global = false,project = true,type=PropertyType.STRING),
		@Property(key =RedmineSettings.BCREF, name =  "Reference Enable",defaultValue="false", description = "set true to enable redmine plugin",global = false,project = true,type=PropertyType.BOOLEAN),
		@Property(key = RedmineSettings.URL, name = "Redmine URL", description = "Example: http://demo.redmine.org/",global = false,project = true),
		@Property(key = RedmineSettings.API_ACCESS_KEY, name = "API Access Key", description = "You can find your API key on your account page ( /my/account ) when logged in, on the right-hand pane of the default layout.", type = org.sonar.api.PropertyType.PASSWORD,global = false,project = true) })
public class RedminePlugin extends SonarPlugin {

	public List getExtensions() {
		return ImmutableList
				.of(
						// Definitions
						RedmineMetrics.class,
						TestExecutionMetrics.class,
						// Batch
						RedmineSensor.class,
						TestExecutionSensor.class,
						RedmineAdapter.class,
						RedmineSettings.class,
						// Server
						RedmineIssueFactory.class,
						// UI
						RedmineWidget.class,
						RedmineBcrefWidget.class,
						TestExecutionWidget.class,
						RedmineSettingsPage.class,
						// Reviews
						RedmineLinkFunction.class,
						RedmineWorkflowBuilder.class,
						// Exceptions
						RedmineGeneralException.class,
						org.sonar.plugins.redmine.exceptions.RedmineAuthenticationException.class,
						org.sonar.plugins.redmine.exceptions.RedmineTransportException.class,
						RedmineNotFoundException.class,
						RedmineNotAuthorizedException.class);
	}
}
