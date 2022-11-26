package guru.hakandurmaz.clients.emailcheck;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    value = "mailchecker"
)
public interface MailClient {

  @GetMapping(path = "api/mail-check/{mailAddress}")
  MailCheckerResponse isIllegal(@PathVariable("mailAddress") String mailAddress);
}
