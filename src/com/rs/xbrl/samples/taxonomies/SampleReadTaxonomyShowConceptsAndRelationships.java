/*
 * © 2009 by Ignacio Hernandez-Ros.
 * This work may be reproduced and redistributed, in whole or in part, 
 * without alteration and without prior written permission, 
 * solely by educational institutions for nonprofit administrative 
 * or educational purposes provided all copies contain the 
 * following statement: 
 * "© 2009 Ignacio Hernandez-Ros. This work is reproduced and distributed with the permission of Ignacio Hernandez-Ros. No other use is permitted without the express prior written permission of Ignacio Hernandez-Ros. For permission, contact ignacio@hernandez-ros.com"
 *
 * File creation date 18/08/2009
 */
/**
 * 
 */
package com.rs.xbrl.samples.taxonomies;

import java.net.URI;
import java.util.Iterator;

import com.ihr.xbrl.om.DTSContainer;
import com.ihr.xbrl.om.exLinks.XBRLRelationship;
import com.ihr.xbrl.om.taxonomy.XBRLItem;
import com.ihr.xbrl.om.taxonomy.XBRLTuple;
import com.ihr.xbrl.om.taxonomy.XMLElementDefinition;

/**
 * @author Ignacio
 *
 */
public class SampleReadTaxonomyShowConceptsAndRelationships {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		/*
		 * Create a new empty DTS Container
		 */
		DTSContainer dts = DTSContainer.newEmptyContainer();
		
		/*
		 * Open the taxonomy DTS from a local file. The DTS is discovered now.
		 */
		URI uri = new URI("sampleTaxonomy.xsd");
		dts.load(uri);
		
		/*
		 * Show taxonomy concepts and relationships
		 */
		Iterator<XMLElementDefinition> iterE = dts.getConcepts();
		while (iterE.hasNext()) {
			XMLElementDefinition el = iterE.next();
			
			System.out.println( "Element name: "+el.getName());
			
			if (el instanceof XBRLItem) {
				System.out.println(" is an XBRL Item");
			} else if (el instanceof XBRLTuple) {
				System.out.println(" is an XBRL Tuple");
			} else {
				System.out.println(" is an XML element that not an XBRL Item an not an XBRL Tuple");
			}
			
			/*
			 *  feel free to access to other concept properties.
			 *  See all them as methods of the XMLElementDefinition object, XBRLItem or XBRLTuple objects 
			 */
			 
			// relationships:
			Iterator<XBRLRelationship> iterFR = el.getFromRelationships();
			while (iterFR.hasNext()) {
				XBRLRelationship rel = iterFR.next();
				
				System.out.println("The element is the source of an XBRL relationship\n");
				System.out.println("Relationship arcrole:"+rel.getArcroleType().getRoleAndArcroleURI().toString());
				System.out.println("Relationship type:"+rel.getType().toString());
				
				/*
				 * feel free to add more information about the relationship. the object is XBRLRelationship
				 */
			}
			
			/*
			 * Exercise: implement the same loop for the relationships the element el is the target.
			 * 
			 * Hint: use the method el.getToRelationships()
			 */

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
