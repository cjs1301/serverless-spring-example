package com.example.NoMoney;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.crac.Core;
import org.crac.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LambdaHandler implements RequestStreamHandler, Resource {
    private static final Logger logger = LoggerFactory.getLogger(LambdaHandler.class);
    public LambdaHandler() {
        // CRaC에 리소스 등록
        Core.getGlobalContext().register(this);
    }
    private static final SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(NoMoneyApplication.class);
        } catch (ContainerInitializationException e) {
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
        JsonNode event = new ObjectMapper().readTree(inputStream);
        
        if (isWarmupRequest(event)) {
            // 워밍업 요청시 추가적인 리소스 초기화
            warmupAdditionalResources();
            return;
        }
        handler.proxyStream(inputStream, outputStream, context);
    }
    @Override
    public void beforeCheckpoint(org.crac.Context<? extends Resource> context) {
        // 체크포인트 전에 필요한 작업 수행
        // 예: 데이터베이스 연결 닫기, 캐시 비우기 등
    }

    @Override
    public void afterRestore(org.crac.Context<? extends Resource> context) {
        // 복원 후 필요한 작업 수행
        // 예: 데이터베이스 연결 재설정, 캐시 재구축 등
    }

    private boolean isWarmupRequest(JsonNode event) {
        return event.has("source") && 
               "serverless-plugin-warmup".equals(event.get("source").asText());
    }
    private void warmupAdditionalResources() {
        logger.info("Warmup request received, initializing additional resources...");
        // 캐시 예열
        // 추가 커넥션 풀 확장 등
    }
}
