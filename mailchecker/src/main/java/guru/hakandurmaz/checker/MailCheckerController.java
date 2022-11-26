package guru.hakandurmaz.checker;

import guru.hakandurmaz.clients.emailcheck.MailCheckerResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/mail-check")
@AllArgsConstructor
@Slf4j
public class MailCheckerController {

  private final MailCheckService mailCheckService;

  @GetMapping("{mailAddress}")
  public MailCheckerResponse isIllegal(@PathVariable("mailAddress") String mailAddress) {
    boolean isIllegalEmail = mailCheckService.isIllegalEmail(mailAddress);
    log.info(mailAddress);
    return new MailCheckerResponse(isIllegalEmail);
  }
}
