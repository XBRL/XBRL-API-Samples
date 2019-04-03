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
package com.rs.xbrl.samples.instances;

import java.net.URI;
import java.util.Properties;

import javax.xml.namespace.QName;

import com.ihr.xbrl.om.DTSContainer;
import com.ihr.xbrl.om.XMLFragment;
import com.ihr.xbrl.om.errors.DTSDiscoveryException;
import com.ihr.xbrl.om.errors.XBRLInconsistentDTSException;
import com.ihr.xbrl.om.errors.XBRLInitializationException;
import com.ihr.xbrl.om.instance.XBRLContext;
import com.ihr.xbrl.om.instance.XBRLEntity;
import com.ihr.xbrl.om.instance.XBRLPeriod;
import com.ihr.xbrl.om.instance.XBRLScenario;
import com.ihr.xbrl.om.table.XBRLTableProcessor;
import com.ihr.xbrl.om.xdt.XDTDimension;
import com.ihr.xbrl.om.xdt.XDTProcessor;
import com.rs.lic.LicenseVerificationException;

/**
 * @author Ignacio
 *
 */
public class CreateContextWithDimensions {
	
	private static final String S2CDIMNS = "http://eiopa.europa.eu/xbrl/s2c/dict/dim";
	private static final String LBDIMNS = "http://eiopa.europa.eu/xbrl/s2c/dict/dom/LB";
	private static final String AMDIMNS = "http://eiopa.europa.eu/xbrl/s2c/dict/dom/AM";

	/**
	 * @param args
	 * @throws LicenseVerificationException 
	 * @throws DTSDiscoveryException 
	 * @throws XBRLInconsistentDTSException 
	 * @throws XBRLInitializationException 
	 */
	public static void main(String[] args) throws LicenseVerificationException, DTSDiscoveryException, XBRLInconsistentDTSException, XBRLInitializationException {
		
		URI entryPoint = URI.create("http://eiopa.europa.eu/eu/xbrl/s2md/fws/pensions/pf-iorps2/2018-11-01/mod/aee.xsd");
		Properties props = new Properties();
		props.setProperty(DTSContainer.PROCESSORS_SEQUENCE, XBRLTableProcessor.PROCESSORNAME);
		DTSContainer dts = DTSContainer.newEmptyContainer(props);
		dts.load(entryPoint);
		
		// dimensions processor. This processor has already explored the entire DTS at load time
		// so you can ask it for information
		// note, on line 61 I explicitly add the XBRL Table Processor. the RS API knows about the processor dependencies and
		// it will add for you the XBRLFormulaProcessor (formulas specification) and the XDTProcessor (dimensions specification)
		// processor are registered once per DTSContainer instance so you normally do not create the processor but obtain the one
		// created during the DTS discovery
		XDTProcessor xdt = dts.getProcessor(XDTProcessor.PROCESSORNAME);
		
		// this is not needed at all. But it is worth including in the example how you can use the existing processors
		// from here you can explore also the dimension members etc...
		// feel free to put a break point on this line and explore what the xdt processor can do for you
		@SuppressWarnings("unused")
		XDTDimension dimBL = xdt.getDimension(new QName(S2CDIMNS,"BL"));
		
		String scheme = "http://standards.iso.org/iso/17442";
		String identifier = "3TKMALJLVDAA5XPZAW6J";
		
		XBRLEntity entity = new XBRLEntity(dts, scheme, identifier, null);
		
		XBRLPeriod period = new XBRLPeriod(dts, "2018-12-31");
		
		XBRLScenario scenario = XBRLScenario.make(dts);
		
		// note also, the prefix is optional, the namespace and local name are required
		XMLFragment d1 = XDTProcessor.getExplicitDimFragment(dts, new QName(S2CDIMNS,"BL","s2c_dim"), new QName(LBDIMNS,"x5029","s2c_LB"));
		scenario.addChild(d1);
		
		XMLFragment d2 = XDTProcessor.getExplicitDimFragment(dts, new QName(S2CDIMNS,"VG","s2c_dim"), new QName(AMDIMNS,"x80","s2c_AM"));
		scenario.addChild(d2);
		
		XBRLContext ctx = new XBRLContext(dts, entity, period, scenario);
		ctx.setId("BLx5029_VGx80");
		
		System.out.println(ctx.toString());
	}

}
