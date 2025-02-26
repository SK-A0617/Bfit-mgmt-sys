package com.bfit.mgmt.service;

import com.bfit.mgmt.util.ApiResponse;

public interface EmailRemService {
	
	/**
	 * Sent Reminder Email
	 * @return
	 */
	ApiResponse sendRemainderMail();

}
