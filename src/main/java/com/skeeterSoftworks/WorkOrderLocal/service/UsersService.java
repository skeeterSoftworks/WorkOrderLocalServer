package com.skeeterSoftworks.WorkOrderLocal.service;


import com.skeeterSoftworks.WorkOrderLocal.to.objects.UserTO;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class UsersService {

	private WebClient webClient;

	@PostConstruct
	private void init() {
		webClient = WebClient.create();
	}

	@Value("${central.url}")
	private String centralUrl;

	public UserTO getUserByQrCode(String qrCode) {

            return webClient.get().uri(centralUrl + "/users/{qrCode}", qrCode)
					.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(UserTO.class).block();
	}
}
