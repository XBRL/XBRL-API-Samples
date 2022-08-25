/*
 * © 2009 by Ignacio Hernandez-Ros.
 * This work may be reproduced and redistributed, in whole or in part, 
 * without alteration and without prior written permission, 
 * solely by educational institutions for nonprofit administrative 
 * or educational purposes provided all copies contain the 
 * following statement: 
 * "© 2009 Ignacio Hernandez-Ros. This work is reproduced and distributed with the permission of Ignacio Hernandez-Ros. No other use is permitted without the express prior written permission of Ignacio Hernandez-Ros. For permission, contact ignacio@hernandez-ros.com"
 *
 * File creation date 26/06/2009
 */
/**
 * 
 */
package com.rs.xbrl.samples.instances;

import java.io.File;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Properties;

import org.apache.logging.log4j.Level;

import com.ihr.xbrl.om.DTSContainer;
import com.ihr.xbrl.om.instance.XBRLFact;
import com.ihr.xbrl.om.instance.XBRLInstance;
import com.ihr.xbrl.om.instance.XBRLContext;

/**
 * Sample, a new instance document is read form the file received as a parameter
 * After the instance object is obtained, the user can start working with it
 * if no parameter is provided the class will read a file whose name is "instance.xbrl"
 * in the same directory where the app is started.
 * <p>
 * After opening the instance document, the class will show all context ids in the instance
 * and will loop over all reported facts (items and tuples)
 * 
 * @author Ignacio
 */
@SuppressWarnings("unused")
public class SampleReadInstance {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {		
		if (args.length == 0) {
			args = new String[1];
			args[0] = "instance.xbrl";
		}
		Properties pros = new Properties();
		DTSContainer dts = DTSContainer.newEmptyContainer(pros);
		XBRLInstance instance = (XBRLInstance)dts.load(Paths.get(args[0]).toUri());
		// ... rest of the application code goes here
		
		Iterator<XBRLContext> iterC = instance.getInstanceRootNode().getContexts();
		while (iterC.hasNext()) {
			System.out.println("Context Id:"+iterC.next().getId());
		}
		
		Iterator<XBRLFact> iterF = instance.getInstanceRootNode().iterator();
		while (iterF.hasNext()) {
			XBRLFact f = iterF.next();
			// ... do dome work with the fact
			System.out.println(f.getLocalPart()+" -> "+f.getStringValue());
		}
		
		
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
