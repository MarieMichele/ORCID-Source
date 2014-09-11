/**
 * =============================================================================
 *
 * ORCID (R) Open Source
 * http://orcid.org
 *
 * Copyright (c) 2012-2013 ORCID, Inc.
 * Licensed under an MIT-Style License (MIT)
 * http://orcid.org/open-source-license
 *
 * This copyright and license information (including a link to the full license)
 * shall be included in its entirety in all copies or substantial portion of
 * the software.
 *
 * =============================================================================
 */
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.09 at 01:52:56 PM BST 
//

package org.orcid.jaxb.model.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.orcid.org/ns/orcid}security-details" minOccurs="0"/>
 *         &lt;element ref="{http://www.orcid.org/ns/orcid}preferences" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.orcid.org/ns/orcid}scope"/>
 *       &lt;attGroup ref="{http://www.orcid.org/ns/orcid}visibility"/>
 *       &lt;attGroup ref="{http://www.orcid.org/ns/orcid}salesforce-id"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "securityDetails", "preferences", "groupOrcidIdentifier", "referredBy" })
@XmlRootElement(name = "orcid-internal")
public class OrcidInternal implements VisibilityType, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @XmlElement(name = "security-details")
    protected SecurityDetails securityDetails;
    protected Preferences preferences;
    @XmlAttribute
    protected Scope scope;
    @XmlAttribute
    protected Visibility visibility;

    @XmlElement(name = "group-orcid-identifier")
    private OrcidIdentifier groupOrcidIdentifier;

    @XmlElement(name = "referred-by")
    private ReferredBy referredBy;

    @XmlElement(name = "salesforce-id")
    private SalesforceId salesforceId;

    /**
     * Gets the value of the securityDetails property.
     * 
     * @return possible object is {@link SecurityDetails }
     * 
     */
    public SecurityDetails getSecurityDetails() {
        return securityDetails;
    }

    /**
     * Sets the value of the securityDetails property.
     * 
     * @param value
     *            allowed object is {@link SecurityDetails }
     * 
     */
    public void setSecurityDetails(SecurityDetails value) {
        this.securityDetails = value;
    }

    /**
     * Gets the value of the preferences property.
     * 
     * @return possible object is {@link Preferences }
     * 
     */
    public Preferences getPreferences() {
        return preferences;
    }

    /**
     * Sets the value of the preferences property.
     * 
     * @param value
     *            allowed object is {@link Preferences }
     * 
     */
    public void setPreferences(Preferences value) {
        this.preferences = value;
    }

    /**
     * Gets the value of the scope property.
     * 
     * @return possible object is {@link Scope }
     * 
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * Sets the value of the scope property.
     * 
     * @param value
     *            allowed object is {@link Scope }
     * 
     */
    public void setScope(Scope value) {
        this.scope = value;
    }

    /**
     * Gets the value of the visibility property.
     * 
     * @return possible object is {@link Visibility }
     * 
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Sets the value of the visibility property.
     * 
     * @param value
     *            allowed object is {@link Visibility }
     * 
     */
    public void setVisibility(Visibility value) {
        this.visibility = value;
    }

    /**
     * Gets the salesforce id property
     * 
     * @return possible object is {@link SalesforceId}
     * */
    public SalesforceId getSalesforceId() {
        return this.salesforceId;
    }

    /**
     * Sets the salesforce id property
     * 
     * @param SalesforceId
     *            allowed object is {@link SalesforceId}
     * */
    public void setSalesforceId(SalesforceId salesforceId) {
        this.salesforceId = salesforceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrcidInternal)) {
            return false;
        }

        OrcidInternal that = (OrcidInternal) o;

        if (preferences != null ? !preferences.equals(that.preferences) : that.preferences != null) {
            return false;
        }
        if (scope != that.scope) {
            return false;
        }
        if (securityDetails != null ? !securityDetails.equals(that.securityDetails) : that.securityDetails != null) {
            return false;
        }
        if (visibility != that.visibility) {
            return false;
        }

        if (referredBy == null) {
            if (that.referredBy != null)
                return false;
        } else if (!referredBy.equals(that.referredBy))
            return false;

        if (salesforceId != null ? !salesforceId.equals(that.salesforceId) : that.salesforceId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = securityDetails != null ? securityDetails.hashCode() : 0;
        result = 31 * result + (preferences != null ? preferences.hashCode() : 0);
        result = result + ((referredBy == null) ? 0 : referredBy.hashCode());
        result = 31 * result + (scope != null ? scope.hashCode() : 0);
        result = 31 * result + (visibility != null ? visibility.hashCode() : 0);
        result = 31 * result + (salesforceId != null ? salesforceId.hashCode() : 0);
        return result;
    }

    public OrcidIdentifier getGroupOrcidIdentifier() {
        return groupOrcidIdentifier;
    }

    public void setGroupOrcidIdentifier(OrcidIdentifier groupOrcidIdentifier) {
        this.groupOrcidIdentifier = groupOrcidIdentifier;
    }

    public ReferredBy getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(ReferredBy referredBy) {
        this.referredBy = referredBy;
    }
}
