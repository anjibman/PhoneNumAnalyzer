package com.makalu.phonenum.core.service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.makalu.phonenum.core.dto.CellularCommand;
import com.makalu.phonenum.core.dto.CellularPrefix;
import com.makalu.phonenum.core.dto.PhoneDto;
import com.makalu.phonenum.core.util.FileUtil;
import com.makalu.phonenum.core.util.TelnetUtil;

public class PhoneNumAnalyzerServiceImpl implements PhoneNumAnalyzerService {
	private final static Logger LOGGER = Logger.getLogger(PhoneNumAnalyzerServiceImpl.class.getName());
	
	CellularPrefix cellularPrefix;
	CellularCommand cellularCommand;
	TelnetUtil telnetUtil;
	
	List<String> successPhoneNum = new ArrayList<String>();
	List<String> failedPhoneNum = new ArrayList<String>();
	List<String> invalidPhoneNum = new ArrayList<String>();
	List<String> repeatedPhoneNum = new ArrayList<String>();
	
	List<String> cmdList = new ArrayList<String>();
	List<String> runCommandList = new ArrayList<String>();
	
	public List<PhoneDto> processPhoneList(List<String> phoneList) {
		List<PhoneDto> phoneDtoList = new ArrayList<PhoneDto>();
		
		init();
		
		for (String phone : phoneList) {
			PhoneDto phoneDto = new PhoneDto();
			System.out.println("Processing for " + phone + " .....");
			if(!isValid(phone) || !isValidCarrier(phone)) {
				phoneDto.setPhoneNumber(phone);
				phoneDto.setStatus("Invalid");
				invalidPhoneNum.add(phone);
			} else {
				phoneDto = sendCommandFor(phone);
			}
			phoneDtoList.add(phoneDto);
			System.out.println("========================================");
		}
		
		generateOutputFiles();
		
		return phoneDtoList;
	}
	
	private PhoneDto sendCommandFor(String phone) {
		PhoneDto phoneDto = new PhoneDto();
		phoneDto.setPhoneNumber(phone);
		runCommandList = generateTemplateCommandList(phone);
		String status = executeCommand(runCommandList);;
		switch (status.toUpperCase()) {
			case "SUCCESS": 
				phoneDto.setStatus("Success");
				successPhoneNum.add(phone);
				break;
			case "FAILED":
				phoneDto.setStatus("Failed");
				failedPhoneNum.add(phone);
				break;
			case "REPEATED":
				phoneDto.setStatus("Already Blacklisted");
				repeatedPhoneNum.add(phone);
				break;
			default:
				LOGGER.info("Invalid phone process status - " + status);
				break;
		}
		return phoneDto;
		
	}

	private void reRunFailed() {
		System.out.println("Re-running failed phone number...");
		for(String phone : failedPhoneNum) {
			System.out.println("Processing for " + phone + " .....");
			sendCommandFor(phone);
			System.out.println("========================================");
		}
		System.out.println("Re-run for failed phone number completes.");
	}

	public void generateOutputFiles() {
		FileUtil fileUtil = new FileUtil();
		Date now = new Date();
		String todaydate = new SimpleDateFormat("yyyyMMdd").format(now);
		String currentTime = new SimpleDateFormat("HHmmss").format(now);
		String todayFolder = fileUtil.getAppBasePath() +  "\\log\\" + todaydate;
		String currentFilePrefix = todayFolder + "\\" + currentTime + "_";
		
		fileUtil.createDir(todayFolder);
		fileUtil.generateCleanFile("output.txt", currentFilePrefix + "result.txt");
		fileUtil.deleteFile("output.txt");
		fileUtil.writeListToFile(successPhoneNum, currentFilePrefix + "success.txt");
		fileUtil.writeListToFile(failedPhoneNum, currentFilePrefix + "error.txt");
		fileUtil.writeListToFile(invalidPhoneNum, currentFilePrefix + "invalid.txt");
		fileUtil.writeListToFile(repeatedPhoneNum, currentFilePrefix + "repeated.txt");
	}

	public CellularPrefix generatePrefix(String fileName) {
		InputStream in = this.getClass().getResourceAsStream(fileName);
		CellularPrefix cellularPrefix = new CellularPrefix();
		try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CellularPrefix.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            cellularPrefix = (CellularPrefix) jaxbUnmarshaller.unmarshal(in);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
		return cellularPrefix;
	}

	public CellularCommand generateCommand(String fileName) {
		InputStream in = this.getClass().getResourceAsStream(fileName);
		CellularCommand cellularCommand = new CellularCommand();
		try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CellularCommand.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            cellularCommand = (CellularCommand) jaxbUnmarshaller.unmarshal(in);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
		return cellularCommand;
	}

	private String executeCommand(List<String> runCommandList) {
		for (String runCmd : runCommandList) {
			System.out.println(runCmd);
			String runCmdStatus = telnetUtil.sendCommand(runCmd);
			if(runCmdStatus.equalsIgnoreCase("REPEATED") || runCmdStatus.equalsIgnoreCase("FAILED")) {
				return runCmdStatus;
			}
		}
		return "SUCCESS";
	}

	private List<String> generateTemplateCommandList(String phone) {
		List<String> cmdList = new ArrayList<String>();
		String phPrefix = phone.substring(0, 3);
		if (cellularPrefix.getNtcPrefixList().contains(phPrefix)) {
			cmdList = cellularCommand.getNtcCommand();
		} else if (cellularPrefix.getUtlPrefixList().contains(phPrefix)) {
			cmdList = cellularCommand.getUtlCommand();
		} else if (cellularPrefix.getSmartPrefixList().contains(phPrefix)) {
			cmdList = cellularCommand.getSmartCommand();
		}
		
		cmdList = generateRunCommandList(cmdList, phone);
		return cmdList;
	}

	private List<String> generateRunCommandList(List<String> cmdList, String phone) {
		List<String> runCommandList = new ArrayList<String>();
		for (String cmd : cmdList) {
			if (cmd.matches("^(.*)NUM=\"0{10}\"(.*)")) {
				String newCmd = cmd.replaceAll("NUM=\"0{10}\"", "NUM=\"" + phone + "\"");
				runCommandList.add(newCmd);
			} else {
				runCommandList.add(cmd);
			}
		}
		return runCommandList;
	}

	private boolean isValidCarrier(String phone) {
		String phPrefix = phone.substring(0, 3);
		List<String> allPrefix = cellularPrefix.getAllPrefixList();
		return allPrefix.contains(phPrefix);
	}

	private boolean isValid(String phone) {
		if (phone.length() != 10) {
			System.out.println(phone + ": Invalid length");
			return false;
		} 
		return true;
	}

	private void init() {
		cellularPrefix = generatePrefix("/MSISDN_Prefix_Plan.xml");
		cellularCommand = generateCommand("/commands.xml");
		telnetUtil = new TelnetUtil("10.70.4.35", 21123, "test_ems", "test123");
	}
}
