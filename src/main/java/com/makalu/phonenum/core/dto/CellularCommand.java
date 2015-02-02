package com.makalu.phonenum.core.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ROOT")
@XmlAccessorType(XmlAccessType.FIELD)
public class CellularCommand {
    @XmlElementWrapper(name = "NTC_Cellular")
    @XmlElement(name = "MSISDN_Command")
    private List<String> ntcCommand;
    @XmlElementWrapper(name = "UTL_Cellular")
    @XmlElement(name = "MSISDN_Command")
    private List<String> utlCommand;
    @XmlElementWrapper(name = "SMART_Cellular")
    @XmlElement(name = "MSISDN_Command")
    private List<String> smartCommand;

    public List<String> getNtcCommand() {
        return ntcCommand;
    }

    public void setNtcCommand(List<String> ntcCommand) {
        this.ntcCommand = ntcCommand;
    }

    public List<String> getUtlCommand() {
        return utlCommand;
    }

    public void setUtlCommand(List<String> utlCommand) {
        this.utlCommand = utlCommand;
    }

    public List<String> getSmartCommand() {
        return smartCommand;
    }

    public void setSmartCommand(List<String> smartCommand) {
        this.smartCommand = smartCommand;
    }
}
