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
package org.sonar.plugins.redmine.ui;

import org.sonar.api.web.AbstractRubyTemplate;
import org.sonar.api.web.RubyRailsWidget;
import org.sonar.api.web.WidgetCategory;

@WidgetCategory("Redmine")
public class TestExecutionWidget extends AbstractRubyTemplate implements RubyRailsWidget {

  public String getId() {
    return "test-execution";
  }

  public String getTitle() {
    return "Test Execution Widget";
  }

  @Override
  protected String getTemplatePath() {
    return "/org/sonar/redmine/test_execution_widget.html.erb";
    //return "C:\\Users\\leonx\\Desktop\\sonar\\sonar-bcref-master\\src\\main\\resources\\org\\sonar\\redmine\\test_execution_widget.html.erb";
  }
}
