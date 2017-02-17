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
import java.util.Properties;
import java.util.Vector;

import com.ihr.xbrl.dts.DTSExplorer;
import com.ihr.xbrl.dts.XBRLTreeRoot;
import com.ihr.xbrl.om.DTSContainer;
import com.ihr.xbrl.om.errors.DTSDiscoveryException;
import com.ihr.xbrl.om.errors.XBRLValidationException;
import com.ihr.xbrl.om.exLinks.DTSBase;
import com.ihr.xbrl.om.exLinks.DefinitionLinkbase;
import com.ihr.xbrl.om.exLinks.XBRLRelationship;
import com.ihr.xbrl.om.taxonomy.XBRLArcroleType;
import com.ihr.xbrl.om.taxonomy.XBRLItem;
import com.ihr.xbrl.om.taxonomy.XBRLRoleType;
import com.ihr.xbrl.om.xdt.XDTProcessor;
import com.rs.lic.LicenseVerificationException;

/**
 * This small application loads a DTS, then it finds all "all" relationships.
 * for each hypercube found it shows its dimensions. and the root domain member of each dimension. 
 * 
 * @author Ignacio
 *
 */
public class SampleShowDimensionalTrees {

	/**
	 * @param args
	 * @throws LicenseVerificationException 
	 * @throws DTSDiscoveryException 
	 * @throws XBRLValidationException 
	 */
	public static void main(String[] args) throws LicenseVerificationException, DTSDiscoveryException, XBRLValidationException {
		
		Properties props = new Properties();
		props.setProperty(DTSContainer.ALWAYS_ADD_ref_2006_02_27_xsd_SCHEMA_TO_DTS, "true");
		
		DTSContainer dts = DTSContainer.newEmptyContainer(props);
		dts.load(Paths.get("taxonomyWithDimensions.xsd").toUri());
		
		DTSExplorer dte = new DTSExplorer(dts);		
		XDTProcessor xdt = XDTProcessor.create(dts);
				
		boolean bShowDim;
		boolean bShowDom;		
		Iterator<XBRLRoleType> iterR = dts.getBaseRoles(DefinitionLinkbase.lbType);
		while (iterR.hasNext()) {
			XBRLRoleType defBaseRole = iterR.next();			
			Vector<XBRLTreeRoot> vRoots = dte.getRoots(defBaseRole.getRoleAndArcroleURI(), DefinitionLinkbase.lbType);
			for (XBRLTreeRoot root : vRoots) {
				
				XBRLArcroleType art = root.getWalker().getArcrole();				
				if (art.equals(xdt.getArcroleTypeAll())) {					
					// hypercube found. Now get all dimensions					
					XBRLRelationship all_rel = root.getWalker().getRelationship();
					XBRLRoleType targetRole = XDTProcessor.getTargetRole(all_rel);
					if (targetRole == null)
						targetRole = defBaseRole;					
					// find hypercube dimension relationships in the base set pointed to by target role
					if (dts.isBaseDefined(DefinitionLinkbase.lbType, targetRole)) {						
						DTSBase base = dts.getBase(DefinitionLinkbase.lbType, targetRole);						
						Iterator<XBRLRelationship> iterR2 = all_rel.getTo().getFromRelationships(xdt.getArcroleTypeHypercubeDimension(), base);
						while (iterR2.hasNext()) {					
							XBRLRelationship hyp_dim_rel = iterR2.next();
							bShowDim = true;
							// find the dimension-domain relationships
							XBRLRoleType targetRole2 = XDTProcessor.getTargetRole(hyp_dim_rel);
							if (targetRole2 == null)
								targetRole2 = targetRole;												
							if (dts.isBaseDefined(DefinitionLinkbase.lbType, targetRole2)) {						
								DTSBase base2 = dts.getBase(DefinitionLinkbase.lbType, targetRole2);						
								Iterator<XBRLRelationship> iterR3 = hyp_dim_rel.getTo().getFromRelationships(xdt.getArcroleTypeDimensionDomain(), base2);								
								while (iterR3.hasNext()) {									
									XBRLRelationship dim_dom_rel = iterR3.next();									
									bShowDom = true;									
									if (bShowDim) {
										bShowDim = false;
										System.out.println("Role: "+hyp_dim_rel.getParentXBRL().getRoleType().getRoleAndArcroleURI());
										System.out.println("Dimension: "+((XBRLItem)hyp_dim_rel.getTo()).getQName());
									}
									if (bShowDom) {
										bShowDom = false;
										System.out.println("Root Domain Member: "+((XBRLItem)dim_dom_rel.getTo()).getQName());
									}
								}
							}							
						}
					}					
				}
			}
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
