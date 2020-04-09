/*
 * © 2010 by Ignacio Hernandez-Ros.
 * This work may be reproduced and redistributed, in whole or in part, 
 * without alteration and without prior written permission, 
 * solely by educational institutions for nonprofit administrative 
 * or educational purposes provided all copies contain the 
 * following statement: 
 * "© 2010 Ignacio Hernandez-Ros. This work is reproduced and distributed with the permission of Ignacio Hernandez-Ros. No other use is permitted without the express prior written permission of Ignacio Hernandez-Ros. For permission, contact ignacio@hernandez-ros.com"
 *
 * File creation date 13/05/2010
 */
/**
 * 
 */
package com.rs.xbrl.samples.taxonomies;

import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.ihr.xbrl.dts.DTSExplorer;
import com.ihr.xbrl.dts.XBRLTreeNode;
import com.ihr.xbrl.dts.XBRLTreeRoot;
import com.ihr.xbrl.om.DTSContainer;
import com.ihr.xbrl.om.errors.DTSDiscoveryException;
import com.ihr.xbrl.om.errors.XBRLValidationException;
import com.ihr.xbrl.om.exLinks.PresentationLinkbase;
import com.ihr.xbrl.om.exLinks.XBRLRelationship;
import com.ihr.xbrl.om.taxonomy.XBRLRoleType;
import com.rs.lic.LicenseVerificationException;

/**
 * This small application loads a DTS, then it shows presentation tree in all presentation extended links
 * 
 * @author Ignacio
 *
 */
public class SampleShowPresentationTrees {
	
	static List<String> languages = new LinkedList<>();
	
	static {
		languages.add("en");
	}

	/**
	 * @param args
	 * @throws LicenseVerificationException 
	 * @throws DTSDiscoveryException 
	 * @throws XBRLValidationException 
	 */
	public static void main(String[] args) throws LicenseVerificationException, DTSDiscoveryException, XBRLValidationException {
		
		if (args.length == 0) {
			args = new String[1];
			args[0] = "sampleTaxonomy.xsd";
		}
		
		Properties props = new Properties();		
		DTSContainer dts = DTSContainer.newEmptyContainer(props);
		dts.load(Paths.get(args[0]).toUri());
		
		DTSExplorer dte = new DTSExplorer(dts);		
				
		Iterator<XBRLRoleType> iterR = dts.getBaseRoles(PresentationLinkbase.lbType);
		while (iterR.hasNext()) {
			XBRLRoleType defBaseRole = iterR.next();			
			List<XBRLTreeRoot> vRoots = dte.getRoots(defBaseRole.getRoleAndArcroleURI(), PresentationLinkbase.lbType);
			for (XBRLTreeRoot root : vRoots)
				recurseShowTree(root,0);
		}
	}

	private static void recurseShowTree(XBRLTreeNode node,int level) {
		if (node instanceof XBRLTreeRoot)
			System.out.println( spaces(level) + node.getLabel(languages.iterator()));
		for (XBRLTreeNode subNode : node.getChildren()) {
			XBRLRelationship rel = node.getWalker().getRelationship();
			System.out.println( "Relationship serialized in "+rel.getParentXBRL().getDocument().getDocumentURI());			
			
			System.out.println( spaces(level) + subNode.getLabel(languages.iterator()));
			recurseShowTree(subNode,level+2);
		}
	}

	private static String spaces(int length) {
		StringBuilder sb = new StringBuilder();
		while (length > 0) {
			sb.append(' ');
			length--;
		}
		return sb.toString();
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
