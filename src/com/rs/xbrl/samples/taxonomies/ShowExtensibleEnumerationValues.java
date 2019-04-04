/*
 * © 2019 by Ignacio Hernandez-Ros.
 *
 * This file is property of Reporting Standard S.L. and shall not be used outside the company unless you
 * have written permissions for different purposes.
 *
 * If you have received this file from Reporting Standard, you may have the following permissions:
 *
 * This work may be reproduced and redistributed, in whole or in part, 
 * without alteration and without prior written permission, 
 * solely by educational institutions for nonprofit administrative 
 * or educational purposes provided all copies contain the 
 * following statement: 
 * "© 2019 Ignacio Hernandez-Ros. This work is reproduced and distributed with the permission of Ignacio Hernandez-Ros. No other use is permitted without the express prior written permission of Ignacio Hernandez-Ros. For permission, contact ignacio@hernandez-ros.com"
 *
 * File creation date Apr 3, 2019
 */
/**
 * 
 */
package com.rs.xbrl.samples.taxonomies;

import java.net.URI;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.namespace.QName;

import com.ihr.xbrl.adins.eba.EBAProcessor;
import com.ihr.xbrl.dts.LabelsProviderInstance;
import com.ihr.xbrl.om.DTSContainer;
import com.ihr.xbrl.om.errors.DTSDiscoveryException;
import com.ihr.xbrl.om.xenum.DomainNode;
import com.ihr.xbrl.om.xenum.ExtensibleEnumerationsProcessor;
import com.rs.lic.LicenseVerificationException;

/**
 * 
 * This example uses the ExtensibleEnumerationsProcessor to show the value space of a concept definition
 * 
 * https://www.reportingstandard.com/apidoc/com/ihr/xbrl/om/xenum/ExtensibleEnumerationsProcessor.html
 * 
 * @author Ignacio
 *
 */
public class ShowExtensibleEnumerationValues {
	
	private static final String S2MDMETNS = "http://eiopa.europa.eu/xbrl/s2md/dict/met";

	/**
	 * @param args
	 * @throws LicenseVerificationException 
	 * @throws DTSDiscoveryException 
	 */
	public static void main(String[] args) throws LicenseVerificationException, DTSDiscoveryException {
		
		URI entryPoint = URI.create("http://eiopa.europa.eu/eu/xbrl/s2md/fws/pensions/pf-iorps2/2018-11-01/mod/aee.xsd");
		Properties props = new Properties();
		
		// EBA taxonomies contains extensions to the XBRL Standard. We have implemented an specific processor called
		// EBAProcessor to facilitate the use of the EBA specific functionality
		// also, the latest version of the EIOPA taxonomy is using Extensible Enumerations to document the value space
		// of a concept definition.
		props.setProperty(DTSContainer.PROCESSORS_SEQUENCE, EBAProcessor.PROCESSORNAME);
		DTSContainer dts = DTSContainer.newEmptyContainer(props);
		dts.load(entryPoint);

		// Let's obtain the instance of the registered processor that handles Extensible Enumerations 
		ExtensibleEnumerationsProcessor eeProc = dts.getProcessor(ExtensibleEnumerationsProcessor.PROCESSORNAME);
		
		// Let's obtain the enumerable domain 
		QName concept = new QName(S2MDMETNS,"ei5100");
		showDomainNodeLabelsRecursive(0, concept, eeProc);
	}
	
	private static void showDomainNodeLabelsRecursive(int level, QName concept, ExtensibleEnumerationsProcessor eeProc) {
		Iterator<DomainNode> iterValues = eeProc.getEnumerableDomain(concept);
		while (iterValues.hasNext()) {
			DomainNode node = iterValues.next();		
			System.out.println(String.format("%s%s -> '%s'",spaces(level*2),node.getConcept().getQName(), LabelsProviderInstance.getLabelsProvider().getLabel(node.getConcept())));
			showDomainNodeLabelsRecursive(level+1, node.getConcept().getQName(), eeProc);
		}
	}

	private static String spaces(int numberOfSpaces) {
		StringBuilder sb = new StringBuilder();
		while (numberOfSpaces > 0) {
			sb.append(' ');
		}
		return sb.toString();
	}
}
