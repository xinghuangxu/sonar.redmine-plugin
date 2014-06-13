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

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.in.ElementFilter;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.api.utils.ParsingUtils;
import org.sonar.api.utils.StaxParser.XmlStreamHandler;
import org.sonar.plugins.redmine.exceptions.EmptyReportException;


public class TestSuiteParser implements XmlStreamHandler {

	private Map<String, TestSuite> testSuites = new HashMap<String, TestSuite>();

	/**
	 * {@inheritDoc}
	 */
	public void stream(SMHierarchicCursor rootCursor) throws XMLStreamException {
		SMInputCursor testSuiteCursor = rootCursor
				.constructDescendantCursor(new ElementFilter("testsuite"));
		try {
			testSuiteCursor.getNext();
		} catch (com.ctc.wstx.exc.WstxEOFException eofExc) {
			throw new EmptyReportException();
		}

		do {
			String testSuiteClassName = testSuiteCursor.getAttrValue("name");
			String testFileName = testSuiteCursor.getAttrValue("filename");

			SMInputCursor testCaseCursor = testSuiteCursor
					.childElementCursor("testcase");
			while (testCaseCursor.getNext() != null) {
				String testClassName = getClassname(testCaseCursor,
						testSuiteClassName);
				String testSuiteKey = isValidKey(testFileName) ? testFileName
						: testClassName;
				TestSuite report = testSuites.get(testSuiteKey);
				if (report == null) {
					report = new TestSuite(testSuiteKey);
					testSuites.put(testSuiteKey, report);
				}
				report.addTestCase(parseTestCaseTag(testCaseCursor));
			}
		} while (testSuiteCursor.getNext() != null);
	}

	private boolean isValidKey(String key) {
		return key != null && !"".equals(key);
	}

	/**
	 * Returns successfully parsed reports as a collection of TestSuite objects.
	 */
	public Collection<TestSuite> getParsedReports() {
		return testSuites.values();
	}

	private String getClassname(SMInputCursor testCaseCursor,
			String defaultClassname) throws XMLStreamException {
		String testClassName = testCaseCursor.getAttrValue("classname");
		return testClassName == null ? defaultClassname : testClassName;
	}

	private TestCase parseTestCaseTag(SMInputCursor testCaseCursor)
			throws XMLStreamException {
		// TODO: get a decent grammar for the junit format and check the
		// logic inside this method against it.

		String name = parseTestCaseName(testCaseCursor);
		Double time = parseTime(testCaseCursor);
		String status = "ok";
		String stack = "";
		String msg = "";

		SMInputCursor childCursor = testCaseCursor.childElementCursor();
		if (childCursor.getNext() != null) {
			String elementName = childCursor.getLocalName();
			if (elementName.equals("skipped")) {
				status = "skipped";
			} else if (elementName.equals("failure")) {
				status = "failure";
				msg = childCursor.getAttrValue("message");
				stack = childCursor.collectDescendantText();
			} else if (elementName.equals("error")) {
				status = "error";
				msg = childCursor.getAttrValue("message");
				stack = childCursor.collectDescendantText();
			}
		}

		return new TestCase(name, time.intValue(), status, stack, msg);
	}

	private double parseTime(SMInputCursor testCaseCursor)
			throws XMLStreamException {
		double time = 0.0;
		try {
			String sTime = testCaseCursor.getAttrValue("time");
			if (!StringUtils.isEmpty(sTime)) {
				Double tmp = ParsingUtils.parseNumber(sTime, Locale.ENGLISH);
				if (!Double.isNaN(tmp)) {
					time = ParsingUtils.scaleValue(tmp * 1000, 3);
				}
			}
		} catch (ParseException e) {
			throw new XMLStreamException(e);
		}

		return time;
	}

	private String parseTestCaseName(SMInputCursor testCaseCursor)
			throws XMLStreamException {
		String name = testCaseCursor.getAttrValue("name");
		String classname = testCaseCursor.getAttrValue("classname");
		if (classname != null) {
			name = classname + "/" + name;
		}
		return name;
	}
}