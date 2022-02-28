
/*
 * SampleService.java 2022. 02. 28
 *
 * Copyright 2022 Naver Cloud Corp. All rights Reserved.
 * Naver Cloud Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package me.potato.springgrpcclient.proxy;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import me.potato.grpc.lib.SampleServiceGrpc;
import me.potato.grpc.lib.SampleServiceOuterClass;

/**
 * @author dongju.paek
 */
@Slf4j
@Service
public class SampleClient {
	private static final int port = 5000;
	private static final String host = "localhost";

	private ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

	private SampleServiceGrpc.SampleServiceStub asyncStub = SampleServiceGrpc.newStub(channel);
	private SampleServiceGrpc.SampleServiceBlockingStub syncStub = SampleServiceGrpc.newBlockingStub(channel);

	// signed
	public void getAllSamplesAsync(int max, String data){
		var req = SampleServiceOuterClass.GetAllSamplesRequest.newBuilder().setMax(max).setData(data).build();

		asyncStub.getAllSamples(req, new StreamObserver<SampleServiceOuterClass.GetAllSamplesResponse>() {
			@Override
			public void onNext(SampleServiceOuterClass.GetAllSamplesResponse value) {
				log.info("onNext {}", value);
			}

			@Override
			public void onError(Throwable t) {
				log.error("onError {}", t.getMessage());
			}

			@Override
			public void onCompleted() {
				log.info("onCompleted");
			}
		});
	}

	//unsigned
	public List<Long> getAllSamples(int max, String data){
		var req = SampleServiceOuterClass.GetAllSamplesRequest.newBuilder().setMax(max).setData(data).build();

		try{
			return syncStub.getAllSamples(req)
				.getSamplesList().stream()
				.map(item -> item.getId() & 0xffffffffL)
				.collect(Collectors.toList());
		}catch (Exception e) {
			log.error(e.getMessage());
		}

		return null;

	}
}