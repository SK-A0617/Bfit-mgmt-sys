package com.bfit.mgmt.serviceImpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bfit.mgmt.entity.BillingInfo;
import com.bfit.mgmt.entity.Member;
import com.bfit.mgmt.repo.BillingInfoRepo;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailReminderService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private BillingInfoRepo billingInfoRepo;

	@Autowired
	private Member member;

	@Value("${spring.mail.username}")
	private String senderMail;

	@Scheduled(cron = "0 0 6 * * ?") // **Scheduled Task: Runs Daily at 6 AM**
	public void chechAndSendRemainderMail() {

		LocalDate today = LocalDate.now();
		LocalDate oneDayBefore = today.minusDays(1);
		LocalDate oneDayAfter = today.plusDays(1);

		List<BillingInfo> membersDueYesterday = billingInfoRepo.findByDueDate(oneDayBefore);
		List<BillingInfo> membersDueToday = billingInfoRepo.findByDueDate(today);
		List<BillingInfo> membersDueTomorrow = billingInfoRepo.findByDueDate(oneDayAfter);

		// 1st Reminder
		for (BillingInfo billingInfo : membersDueYesterday) {
			sendReminderEmail(member.getEmail(), "Payment Reminder: Due Tomorrow!", "Dear " + member.getMemberName()
					+ ",\nYour gym fee is due tomorrow (" + billingInfo.getDueDate() + "). Please make your payment.");
		}

		// 2nd Reminder
		for (BillingInfo billingInfo : membersDueToday) {
			sendReminderEmail(member.getEmail(), "Payment Due Today!", "Dear " + member.getMemberName()
					+ ",\nYour gym fee is due today (" + billingInfo.getDueDate() + "). Please make your payment.");
		}

		// 3rd/Final Reminder
		for (BillingInfo billingInfo : membersDueTomorrow) {
			sendReminderEmail(member.getEmail(), "Final Reminder: Payment Overdue!",
					"Dear " + member.getMemberName() + ",\nYour gym fee was due on (" + billingInfo.getDueDate()
							+ "). Please pay immediately to avoid service disruption.");
		}
	}

	// Send Remainder Email
	private void sendReminderEmail(String toMail, String subject, String body) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(senderMail);
			helper.setTo(toMail);
			helper.setSubject(subject);
			helper.setText(body, true);

			javaMailSender.send(message);
			System.out.println("Reminder email sent to: " + toMail);

		} catch (Exception e) {
			System.out.println("Error sending email: " + e.getMessage());
		}
	}

}
