package com.bfit.mgmt.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bfit.mgmt.entity.BillingInfo;
import com.bfit.mgmt.entity.Member;
import com.bfit.mgmt.repo.BillingInfoRepo;
import com.bfit.mgmt.repo.MemberRepo;
import com.bfit.mgmt.service.EmailRemService;
import com.bfit.mgmt.util.ApiResponse;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailReminderService implements EmailRemService{

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private BillingInfoRepo billingInfoRepo;

	@Autowired
	private MemberRepo memberRepo;

	@Value("${spring.mail.username}")
	private String senderMail;

	@Override
	@Scheduled(cron = "0 * * * * ?", zone = "Asia/Kolkata")// **Scheduled Task: Runs Daily at 6:30 AM**
	public ApiResponse sendRemainderMail() {
		
		log.info("***** Start Triggering Reminder Email Service *****");

		LocalDate today = LocalDate.now();
		LocalDate oneDayBefore = today.minusDays(1);
		LocalDate oneDayAfter = today.plusDays(1);
		
		try {
			List<BillingInfo> membersDueYesterday = billingInfoRepo.findByDueDate(oneDayBefore);
			List<BillingInfo> membersDueToday = billingInfoRepo.findByDueDate(today);
			List<BillingInfo> membersDueTomorrow = billingInfoRepo.findByDueDate(oneDayAfter);
			
			Set<UUID> memberIds = Stream.of(membersDueYesterday, membersDueToday, membersDueTomorrow)
					.flatMap(List::stream)
					.map(BillingInfo::getMemberId)
					.collect(Collectors.toSet());
			
			Map<UUID, Member> memberMap = memberRepo.findByIdIn(memberIds)
					.stream()
					.collect(Collectors.toMap(Member::getId, Function.identity()));

			// 1st Reminder
			if(ObjectUtils.isNotEmpty(today.minusDays(1))) {
				membersDueYesterday.forEach(billingInfo ->
				sendReminderEmail(memberMap.get(billingInfo.getMemberId()), "Payment Reminder: Due Tomorrow!", "Dear " + memberMap.get(billingInfo.getMemberId()).getMemberName()
						+ ",\nYour gym fee is due tomorrow (" + billingInfo.getDueDate() + "). Please make your payment."));				
			}

			// 2nd Reminder
			if(ObjectUtils.isNotEmpty(today)) {
				membersDueToday.forEach(billingInfo ->
				sendReminderEmail(memberMap.get(billingInfo.getMemberId()), "Payment Due Today!", "Dear " + memberMap.get(billingInfo.getMemberId()).getMemberName()
						+ ",\nYour gym fee is due today (" + billingInfo.getDueDate() + "). Please make your payment."));
			}
				
			// 3rd/Final Reminder
			if(ObjectUtils.isNotEmpty(today.plusDays(1))) {
				membersDueTomorrow.forEach(billingInfo ->
				sendReminderEmail(memberMap.get(billingInfo.getMemberId()), "Final Reminder: Payment Overdue!",
						"Dear " + memberMap.get(billingInfo.getMemberId()).getMemberName() + ",\nYour gym fee was due on (" + billingInfo.getDueDate()
								+ "). Please pay immediately to avoid service disruption."));
			}
			return new ApiResponse(HttpStatus.OK, "Reminder email sent", false);
		}catch(Exception exception) {
			return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while send the reminder email", true);
		}
	}

	// Send Remainder Email
	private void sendReminderEmail(Member member, String subject, String body){
		
		if(ObjectUtils.isEmpty(member)) {
			log.warn("Skipping email - Member not found");
			return;
		}
		
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(senderMail);
			helper.setTo(member.getEmail());
			helper.setSubject(subject);
			helper.setText(body, true);

			javaMailSender.send(message);
			log.info("Reminder email sent to: {}", member.getEmail());

		} catch (Exception e) {
			log.error("Failed to send email to {}: {}", member.getEmail(), e.getMessage());
		}
	}

}
