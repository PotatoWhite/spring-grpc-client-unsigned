
/*
 * TestController.java 2022. 02. 28
 *
 * Copyright 2022 Naver Cloud Corp. All rights Reserved.
 * Naver Cloud Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package me.potato.springgrpcclient.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.potato.springgrpcclient.proxy.SampleClient;

/**
 * @author dongju.paek
 */
@RequiredArgsConstructor
@RestController
public class TestController {

	private final SampleClient sampleService;

	@GetMapping("/sync")
	public List<Long> GetAllSamples(@RequestParam int max, @RequestParam String data){
		return sampleService.getAllSamples(max, data);

	}

	@GetMapping("/async")
	public void GetAllSamplesAsync(@RequestParam int max, @RequestParam String data){
		sampleService.getAllSamplesAsync(max, data);

	}
}