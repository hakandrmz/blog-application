package guru.hakandurmaz.checker;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/mail-check")
@AllArgsConstructor
public class MailCheckerController {

    private final MailCheckService mailCheckService;

    @GetMapping("{mailAddress}")
    public MailCheckerResponse isIllegal(@PathVariable("mailAddress") String mailAddress) {
       boolean isIllegalEmail = mailCheckService.isIllegalEmail(mailAddress);
       return new MailCheckerResponse(isIllegalEmail);
    }
}
