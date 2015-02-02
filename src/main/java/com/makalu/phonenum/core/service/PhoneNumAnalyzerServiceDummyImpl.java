package com.makalu.phonenum.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.makalu.phonenum.core.dto.PhoneDto;

public class PhoneNumAnalyzerServiceDummyImpl implements PhoneNumAnalyzerService {

	public List<PhoneDto> processPhoneList(List<String> phoneList) {
		List<PhoneDto> phoneDtoList = new ArrayList<PhoneDto>();
		for(String phone : phoneList) {
			PhoneDto phoneDto = new PhoneDto();
			phoneDto.setPhoneNumber(phone);
			phoneDto.setStatus(generateStatus());
			phoneDtoList.add(phoneDto);
		}
		return phoneDtoList;
	}
	
	private String generateStatus() {
		Random random = new Random();
		Long randomNum = createRandomInteger(1L, 4L, random);
		switch (randomNum.intValue()) {
			case 1: return "Success";
			case 2: return "Failed";
			case 3: return "Invalid";
			default: return "Already Blacklisted";
		}
	}

	private Long createRandomInteger(Long aStart, Long aEnd, Random aRandom) {
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		// get the range, casting to long to avoid overflow problems
		Long range = aEnd - aStart + 1;
		// compute a fraction of the range, 0 <= frac < range
		Long fraction = (long) (range * aRandom.nextDouble());
		Long randomNumber = fraction + aStart;
		return randomNumber;
	}

}
