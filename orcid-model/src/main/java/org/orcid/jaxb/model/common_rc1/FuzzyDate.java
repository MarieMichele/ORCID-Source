/**
 * =============================================================================
 *
 * ORCID (R) Open Source
 * http://orcid.org
 *
 * Copyright (c) 2012-2014 ORCID, Inc.
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
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.19 at 01:44:12 PM BST 
//

package org.orcid.jaxb.model.common_rc1;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for fuzzy-date complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fuzzyDate", propOrder = { "year", "month", "day" }, namespace = "http://www.orcid.org/ns/common")
@XmlSeeAlso({ PublicationDate.class })
public class FuzzyDate implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected Year year;
    protected Month month;
    protected Day day;

    public FuzzyDate() {

    }

    public FuzzyDate(Year year, Month month, Day day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public FuzzyDate(Integer year, Integer month, Integer day) {
        this.year = year != null ? Year.valueOf(year) : null;
        this.month = month != null ? new Month(month) : null;
        this.day = day != null ? new Day(day) : null;
    }

    public FuzzyDate(FuzzyDate other) {
        this.year = other.year;
        this.month = other.month;
        this.day = other.day;
    }

    public FuzzyDate(org.orcid.jaxb.model.message.FuzzyDate other) {
        if (other == null)
            throw new IllegalArgumentException("Invalid init parameter");
        if (other.getYear() != null) {
            this.year = new Year();
            this.year.setValue(other.getYear().getValue());
        }
        if (other.getMonth() != null) {
            this.month = new Month();
            this.month.setValue(other.getMonth().getValue());
        }
        if (other.getDay() != null) {
            this.day = new Day();
            this.day.setValue(other.getDay().getValue());
        }
    }

    /**
     * Gets the value of the year property.
     * 
     * @return possible object is {@link Year }
     * 
     */
    public Year getYear() {
        return year;
    }

    /**
     * Sets the value of the year property.
     * 
     * @param value
     *            allowed object is {@link Year }
     * 
     */
    public void setYear(Year value) {
        this.year = value;
    }

    /**
     * Gets the value of the month property.
     * 
     * @return possible object is {@link Month }
     * 
     */
    public Month getMonth() {
        return month;
    }

    /**
     * Sets the value of the month property.
     * 
     * @param value
     *            allowed object is {@link Month }
     * 
     */
    public void setMonth(Month value) {
        this.month = value;
    }

    /**
     * Gets the value of the day property.
     * 
     * @return possible object is {@link Day }
     * 
     */
    public Day getDay() {
        return day;
    }

    /**
     * Sets the value of the day property.
     * 
     * @param value
     *            allowed object is {@link Day }
     * 
     */
    public void setDay(Day value) {
        this.day = value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(year.getValue());
        if (month != null) {
            builder.append("-");
            builder.append(month.getValue());
        }
        if (day != null) {
            builder.append("-");
            builder.append(day.getValue());
        }
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((day == null) ? 0 : day.hashCode());
        result = prime * result + ((month == null) ? 0 : month.hashCode());
        result = prime * result + ((year == null) ? 0 : year.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FuzzyDate other = (FuzzyDate) obj;
        if (day == null) {
            if (other.day != null)
                return false;
        } else if (!day.equals(other.day))
            return false;
        if (month == null) {
            if (other.month != null)
                return false;
        } else if (!month.equals(other.month))
            return false;
        if (year == null) {
            if (other.year != null)
                return false;
        } else if (!year.equals(other.year))
            return false;
        return true;
    }

}
