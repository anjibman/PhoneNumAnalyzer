package com.makalu.phonenum.core.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ROOT")
@XmlAccessorType(XmlAccessType.FIELD)
public class CellularPrefix {

    @XmlElementWrapper(name = "NTC_Cellular")
    @XmlElement(name = "MSISDN_Prefix")
    private List<String> ntcPrefixList;

    @XmlElementWrapper(name = "UTL_Cellular")
    @XmlElement(name = "MSISDN_Prefix")
    private List<String> utlPrefixList;

    @XmlElementWrapper(name = "SMART_Cellular")
    @XmlElement(name = "MSISDN_Prefix")
    private List<String> smartPrefixList;

    public List<String> getNtcPrefixList() {
        return ntcPrefixList;
    }

    public void setNtcPrefixList(List<String> ntcPrefixList) {
        this.ntcPrefixList = ntcPrefixList;
    }

    public List<String> getUtlPrefixList() {
        return utlPrefixList;
    }

    public void setUtlPrefixList(List<String> utlPrefixList) {
        this.utlPrefixList = utlPrefixList;
    }

    public List<String> getSmartPrefixList() {
        return smartPrefixList;
    }

    public void setSmartPrefixList(List<String> smartPrefixList) {
        this.smartPrefixList = smartPrefixList;
    }
    
    public List<String> getAllPrefixList() {
    	List<String> allPrefixList = new ArrayList<String>(ntcPrefixList);
    	allPrefixList.addAll(utlPrefixList);
    	allPrefixList.addAll(smartPrefixList);
    	return allPrefixList;
    }
}
