/*
 * © 2009 by Ignacio Hernandez-Ros.
 * This work may be reproduced and redistributed, in whole or in part, 
 * without alteration and without prior written permission, 
 * solely by educational institutions for nonprofit administrative 
 * or educational purposes provided all copies contain the 
 * following statement: 
 * "© 2009 Ignacio Hernandez-Ros. This work is reproduced and distributed with the permission of Ignacio Hernandez-Ros. No other use is permitted without the express prior written permission of Ignacio Hernandez-Ros. For permission, contact ignacio@hernandez-ros.com"
 *
 * File creation date 04/08/2009
 */
/**
 * 
 */
package com.rs.xbrl.samples.validation;

import java.net.URI;
import java.util.Properties;

import com.ihr.xbrl.om.DTSContainer;
import com.ihr.xbrl.om.XBRLCoreProcessor;
import com.ihr.xbrl.om.XBRLValidationReport;

/**
 * This example demonstrates how to use Reporting Standard XBRL API for doing fast XBRL Validation.
 * 
 * In Fast Validation mode, the taxonomy is not loaded each time a document must be validated. It is
 * loaded once and it is reused for the validation of documents using the same DTS. If the DTS is not
 * changed, then new documents can be validated without having to load the DTS again.
 * 
 * @author Ignacio
 *
 */
public class SampleValidation {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		Properties props = new Properties();
		
		// Configure what processors shall be executed and in which order	
		// Standard processors available are
		
		//		XDTProcessor.PROCESSORNAME for dimensions 1.0
		//		XBRLFormulaProcessor.PROCESSORNAME for formula processor
		//		ExtensibleEnumerationsProcessor.PROCESSORNAME for extensible enumerations specification
		//		UTRProcessor.PROCESSORNAME for type validation according with with the Units Type Registry
		//		XBRLTableProcessor.PROCESSORNAME for the table linkbase specification
		//		EFMProcessor.PROCESSORNAME for USA SEC validation
		//
		//		Custom processors can be developed and added to the validation chain. 
		//		Custom processors are common, for example, when the company submits a report to a regulator
		//		on the regulator web site and the regulator requires to validate the user logged in to the
		//		web site is authorized to submit a report for the corresponding scheme/identifier in all
		//		report contexts
		//		
		props.setProperty(DTSContainer.PROCESSORS_SEQUENCE, XBRLCoreProcessor.PROCESSORNAME);
		
		// Load the taxonomy first.
		DTSContainer dts = DTSContainer.newEmptyContainer(props);
		dts.load(new URI("instance.xbrl")); // open the report to be validated
		
		// this runs the entire validation chain and collect validation errors and inconsistencies
		XBRLValidationReport report = dts.validate(); 

		// this generates a simple text validation report
		System.out.println(report.toString());
		
		// this generates a validation report in Excel format with no errors nor inconsistencies
		// this requires ApachePOI on the classpath
//		FileOutputStream fos = new FileOutputStream("Validation.xlsx");
//		XBRLValidationReportToExcel.serializeToOutputStream(report, fos, true, null);
//		fos.close();
	}

}

//
// This software is distributed on an "AS IS" basis,
// WITHOUT WARRANTY OF ANY KIND, either express or implied.
//
// The Original Code is: all this file.
//
// The Initial Developer of the Original Code is Ignacio Hernandez-Ros.
//
// Contributor(s): none.
