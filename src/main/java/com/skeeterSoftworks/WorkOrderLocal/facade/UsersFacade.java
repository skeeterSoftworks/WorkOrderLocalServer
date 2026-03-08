package com.skeeterSoftworks.WorkOrderLocal.facade;


import com.skeeterSoftworks.WorkOrderLocal.service.UsersService;
import com.skeeterSoftworks.WorkOrderLocal.to.objects.UserTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UsersFacade {


	UsersService usersService;


	@Autowired
	public UsersFacade(UsersService usersService) {
		this.usersService = usersService;
	}


	@GetMapping("/{qrCode}")
	public ResponseEntity<?> getSingleUser(@PathVariable String qrCode) {

		log.debug("Facade call: getSingleUser({})", qrCode);

		try {
			if (!StringUtils.hasText(qrCode)) {
				log.error("Invalid input params!: {}", qrCode);
				return ResponseEntity.badRequest().build();
			}

			return ResponseEntity.ok(usersService.getUserByQrCode(qrCode));

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.internalServerError().build();
		}
	}


}
