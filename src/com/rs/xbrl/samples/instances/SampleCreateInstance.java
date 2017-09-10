/*
 * © 2008 by Ignacio Hernandez-Ros.
 * This work may be reproduced and redistributed, in whole or in part, 
 * without alteration and without prior written permission, 
 * solely by educational institutions for nonprofit administrative 
 * or educational purposes provided all copies contain the 
 * following statement: 
 * "© 2008 Ignacio Hernandez-Ros. This work is reproduced and distributed with the permission of Ignacio Hernandez-Ros. No other use is permitted without the express prior written permission of Ignacio Hernandez-Ros. For permission, contact ignacio@hernandez-ros.com"
 *
 * File creation date 26/01/2008
 */
/**
 * 
 */
package com.rs.xbrl.samples.instances;

import java.net.URI;
import java.util.Vector;

import javax.xml.namespace.QName;

import com.ihr.xbrl.om.DTSContainer;
import com.ihr.xbrl.om.XBRL;
import com.ihr.xbrl.om.XBRLCoreProcessor;
import com.ihr.xbrl.om.XBRLValidationReport;
import com.ihr.xbrl.om.instance.XBRLContext;
import com.ihr.xbrl.om.instance.XBRLEntity;
import com.ihr.xbrl.om.instance.XBRLFactNonNumeric;
import com.ihr.xbrl.om.instance.XBRLFactNumeric;
import com.ihr.xbrl.om.instance.XBRLInstance;
import com.ihr.xbrl.om.instance.XBRLNumber;
import com.ihr.xbrl.om.instance.XBRLNumber.ExactitudeMode;
import com.ihr.xbrl.om.instance.XBRLPeriod;
import com.ihr.xbrl.om.instance.XBRLUnit;
import com.ihr.xbrl.om.taxonomy.XBRLItem;

import net.sf.saxon.value.StringValue;

/**
 * 
 * Demonstration about how to programmatically create an instance document
 * 
 * Includes XBRL 2.1 validation before saving
 * 
 * @author Ignacio
 *
 */
public class SampleCreateInstance {
	
	private final static String ns = "http://www.reportingstandard.com/sampleTaxonomy"; 

	public static void main(String args[]) throws Exception {
		
		// Loads the sample taxonomy
		DTSContainer dts = DTSContainer.newEmptyContainer();
		URI uri = new URI("sampleTaxonomy.xsd");
		dts.load(uri);
		
		// Create the instance document
		XBRLInstance instance = new XBRLInstance(dts);
		
		// create a context for the instant "Begining of year 2008"
		XBRLEntity ent = new XBRLEntity(dts,"http://www.xbrl.org/scheme","Sample Company",null);
		XBRLPeriod p = new XBRLPeriod(dts,"2008-01-01");
		XBRLContext ctx = new XBRLContext(dts,ent,p,null);
		
		// create the unit for monetary item type values
		Vector<QName> vU = new Vector<QName>(1);
		vU.add(new QName(XBRL.XBRL_iso4217NS,"EUR"));
		XBRLUnit unit = new XBRLUnit(dts,vU,null);
		
		// Obtain the concept definition for concept "A"
		XBRLItem itemA = (XBRLItem)dts.getConcept(new QName(ns,"A"));
		
		// create the Non Numeric (Text) fact item
//		XBRLFactNonNumeric fA = new XBRLFactNonNumeric(instance,ctx,itemA);
		XBRLFactNonNumeric fA = new XBRLFactNonNumeric(instance.getInstanceRootNode(), ctx, itemA, true);
		// fA is nil until a value is assigned
		fA.setValue(new StringValue("test"));
		
		// creare a Numeric fact item
		XBRLItem itemB = (XBRLItem)dts.getConcept(new QName(ns,"B"));
//		XBRLFactNumeric fB = new XBRLFactNumeric(instance,ctx,itemB,unit);
		XBRLFactNumeric.make(instance.getInstanceRootNode(), ctx, itemB, unit, 1000, XBRLNumber.INF, ExactitudeMode.DECIMALS, null, true);
		
		XBRLItem itemC = (XBRLItem)dts.getConcept(new QName(ns,"C"));
		XBRLFactNumeric.make(instance.getInstanceRootNode(), ctx, itemC, unit,  700, XBRLNumber.INF, ExactitudeMode.DECIMALS, null, true);
		
		XBRLItem itemD = (XBRLItem)dts.getConcept(new QName(ns,"D"));
		XBRLFactNumeric.make(instance.getInstanceRootNode(), ctx, itemD, unit,  300, XBRLNumber.INF, ExactitudeMode.DECIMALS, null, true);

		// Validate the report and obtain the results in the report object
		XBRLValidationReport rep = instance.validate(XBRLCoreProcessor.create(dts));
		
		// if the report is invalid, then throw XBRLExceptions...
		rep.throwExceptions();
		// do the same for calculation inconsistencies
		rep.inconsistenciesAsExceptions();
		
		// if everything was right, then serialize the report to disk.
		instance.setURI(new URI("instance_created.xbrl"));
		instance.save();
		
		System.exit(0);
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
