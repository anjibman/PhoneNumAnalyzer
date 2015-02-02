package com.makalu.phonenum.core.service;

import java.util.List;

import com.makalu.phonenum.core.dto.PhoneDto;

public interface PhoneNumAnalyzerService {
	public List<PhoneDto> processPhoneList(List<String> phoneList);
}
