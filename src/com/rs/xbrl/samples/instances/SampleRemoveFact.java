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

import com.ihr.xbrl.om.DTSContainer;
import com.ihr.xbrl.om.instance.XBRLFact;
import com.ihr.xbrl.om.instance.XBRLInstance;

/**
 * 
 * Demonstration about how an application can create an instance document
 * and include a footnote for a fact item.
 * 
 * @author Ignacio
 *
 */
public class SampleRemoveFact {
	
	public static void main(String args[]) throws Exception {
		
		// Loads the sample taxonomy
		DTSContainer dts = DTSContainer.newEmptyContainer();
		URI uri = new URI("instance.xbrl");
		XBRLInstance instance = (XBRLInstance)dts.load(uri);

		System.out.println("Before remove fact");
		System.out.println(instance);
		
		XBRLFact fact = instance.getInstanceRootNode().get(0);
		instance.getInstanceRootNode().remove(fact);
		
		System.out.println("After remove fact");
		System.out.println(instance);
		//instance.save(/*true*/);
		
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
