package com.bfit.mgmt.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import com.bfit.mgmt.util.ApiResponse;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailReminderService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private BillingInfoRepo billingInfoRepo;

	@Autowired
	private MemberRepo memberRepo;

	@Value("${spring.mail.username}")
	private String senderMail;

	@Scheduled(cron = "0 0 6 * * ?") // **Scheduled Task: Runs Daily at 6:00 AM**
	public ApiResponse sendRemainderMail() {

		log.info("***** Start Triggering Reminder Email Service *****");

		LocalDate today = LocalDate.now();
		LocalDate onDayBefore = today.minusDays(1);
		LocalDate onDayAfter = today.plusDays(1);
		
		try {
			List<BillingInfo> membersOverDue = billingInfoRepo.findByDueDate(onDayBefore);
			List<BillingInfo> membersDueToday = billingInfoRepo.findByDueDate(today);
			List<BillingInfo> membersDueTomorrow = billingInfoRepo.findByDueDate(onDayAfter);

			// 1st Reminder (Before Day)
			if (ObjectUtils.isNotEmpty(membersDueTomorrow)) {
				List<Member> memberResp = new ArrayList<>();
				for (BillingInfo billingInfoObj : membersDueTomorrow) {
					memberResp.add(memberRepo.findByIdAndStatus(billingInfoObj.getMemberId()));
				}
				log.info("1st Reminder (Before Day)");
				
				Map<UUID, Member> memberMap = memberResp.stream()
						.collect(Collectors.toMap(Member::getId, Function.identity()));

				membersDueTomorrow.forEach(billingInfo -> sendReminderEmail(memberMap.get(billingInfo.getMemberId()),
						"BFIT GYM - Payment Reminder: Due Tomorrow!",
						"<html>" + "<body style='font-family: Arial, sans-serif;'>" + "<p>Dear <strong>"
								+ memberMap.get(billingInfo.getMemberId()).getMemberName() + "</strong>,</p>"
								+ "<p>Your gym fee is due tomorrow (<strong>" + billingInfo.getDueDate()
								+ "</strong>).</p>" + "<p>Your due amount is <strong style='color: #d9534f;'>"
								+ billingInfo.getBalanceAmount() + " Rs</strong>.</p>"
								+ "<p>Please make your payment at the earliest.</p>" + "<br>" + "<p>Best Regards,</p>"
								+ "<p><strong>BFIT GYM</strong></p>" + "</body>" + "</html>"));
			}

			// 2nd Reminder (On Day)
			if (ObjectUtils.isNotEmpty(membersDueToday)) {
				List<Member> memberResp = new ArrayList<>();
				for (BillingInfo billingInfoObj : membersDueToday) {
					memberResp.add(memberRepo.findByIdAndStatus(billingInfoObj.getMemberId()));
				}
				log.info("2nd Reminder (On Day)");
				
				Map<UUID, Member> memberMap = memberResp.stream()
						.collect(Collectors.toMap(Member::getId, Function.identity()));

				membersDueToday.forEach(billingInfo -> sendReminderEmail(memberMap.get(billingInfo.getMemberId()),
						"BFIT GYM - Payment Due Today!",
						"<html>" + "<body style='font-family: Arial, sans-serif;'>" + "<p>Dear <strong>"
								+ memberMap.get(billingInfo.getMemberId()).getMemberName() + "</strong>,</p>"
								+ "<p>Your gym fee is due today (<strong>" + billingInfo.getDueDate()
								+ "</strong>).</p>" + "<p>Your due amount is <strong style='color: #d9534f;'>"
								+ billingInfo.getBalanceAmount() + " Rs</strong>.</p>"
								+ "<p>Please make your payment.</p>" + "<br>" + "<p>Best Regards,</p>"
								+ "<p><strong>BFIT GYM</strong></p>" + "</body>" + "</html>"));
			}

			// 3rd Reminder (After Day)
			if (ObjectUtils.isNotEmpty(membersOverDue)) {
				List<Member> memberResp = new ArrayList<>();
				for (BillingInfo billingInfoObj : membersOverDue) {
					memberResp.add(memberRepo.findByIdAndStatus(billingInfoObj.getMemberId()));
				}
				log.info("3rd Reminder (After Day)");
				
				Map<UUID, Member> memberMap = memberResp.stream()
						.collect(Collectors.toMap(Member::getId, Function.identity()));
				
				membersOverDue.forEach(billingInfo -> sendReminderEmail(memberMap.get(billingInfo.getMemberId()),
						"BFIT GYM - Final Reminder: Payment Overdue!",
						"<html>" + "<body style='font-family: Arial, sans-serif;'>" + "<p>Dear <strong>"
								+ memberMap.get(billingInfo.getMemberId()).getMemberName() + "</strong>,</p>"
								+ "<p>Your gym fee was due on (<strong>" + billingInfo.getDueDate() + "</strong>).</p>"
								+ "<p>Your due amount is <strong style='color: #d9534f;'>"
								+ billingInfo.getBalanceAmount() + " Rs</strong>.</p>"
								+ "<p>Please pay immediately to avoid service disruption.</p>" + "<br>"
								+ "<p>Best Regards,</p>" + "<p><strong>BFIT GYM</strong></p>" + "</body>" + "</html>"));
			}

			return new ApiResponse(HttpStatus.OK, "Reminder email sent", false);
		} catch (Exception exception) {
			return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while send the reminder email", true);
		}
	}

	// Send Remainder Email
	private void sendReminderEmail(Member member, String subject, String body) {

		if (ObjectUtils.isEmpty(member) || ObjectUtils.isEmpty(member.getEmail())) {
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
