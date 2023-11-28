package com.example.questionnaire.config;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@EnableCaching //Spring framework ���Ψ��X�ʽw�s������,�e�����ܤ֦��@��CacheManager��Bean
@Configuration//�t�m���]�w��,�å��spring boot�U��
public class CaffeineCacheConfig {
	@Bean
	public CacheManager cacheManager() {
		CaffeineCacheManager  cacheManager = new CaffeineCacheManager();
		cacheManager.setCaffeine(Caffeine.newBuilder()
		//�]�w�L���ɶ�,�C���B�z(�g��Ū)��,�T�w�ɶ������A���ʧ@,�֨���ƴN�|����
		.expireAfterAccess(600,TimeUnit.SECONDS)
		//��l�����ЪŶ��j�p
		.initialCapacity(100)
		//�Ȧs���̤j����
		.maximumSize(500));
		
		return cacheManager;
	}

		
		
}


