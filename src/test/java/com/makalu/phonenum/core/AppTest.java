package com.makalu.phonenum.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.makalu.phonenum.core.dto.CellularCommand;
import com.makalu.phonenum.core.dto.CellularPrefix;
import com.makalu.phonenum.core.service.PhoneNumAnalyzerServiceImpl;
import com.makalu.phonenum.core.util.FileUtil;

public class AppTest {
	
	PhoneNumAnalyzerServiceImpl phoneNumAnalyzerServiceImpl = new PhoneNumAnalyzerServiceImpl();
	
	@Test
	public void testGeneratePrefixListFromFile() {
		CellularPrefix cellularPrefix = phoneNumAnalyzerServiceImpl.generatePrefix("/MSISDN_Prefix_Plan.xml");
		assertEquals(cellularPrefix.getAllPrefixList().size(), 6);
	}
	
	@Test
	public void testGenerateCmdTemplatefromFile() {
		CellularCommand commandTemplate = phoneNumAnalyzerServiceImpl.generateCommand("/commands.xml");
		assertEquals(commandTemplate.getNtcCommand().size(), 10);
		assertEquals(commandTemplate.getUtlCommand().size(), 5);
		assertEquals(commandTemplate.getSmartCommand().size(), 10);
	}
	
	@Test
	public void testCoreBasePath() {
		FileUtil fileUtil = new FileUtil();
		assertNotNull(fileUtil.getAppBasePath());
		System.out.println(fileUtil.getAppBasePath());
	}

}
