package ru.otus.core.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.core.dto.ClientForm;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.services.DBServiceClient;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final DBServiceClient dbServiceClient;

    /**
     * Метод обработки главной страницы (список клиентов)
     */
    @GetMapping(value = {"/", "/clients"})
    public String showClientsList(Model model) {
        var clients = dbServiceClient.findAll();
        log.info("Found {} clients", clients.size());
        model.addAttribute("clients", clients);
        model.addAttribute("newClient", new ClientForm());
        return "clients";
    }

    /**
     * Метод обработки формы создания клиента
     */
    @PostMapping("/client/create")
    public RedirectView createClient(@ModelAttribute("newClient") ClientForm form) {
        var client = new Client(
                null,
                form.getName(),
                new Address(null, form.getAddressStreet()),
                mapToPhoneList(form.getPhoneNumbers()),
                true
        );

        log.info("Save client from {}", client);
        dbServiceClient.saveClient(client);
        return new RedirectView("/", true);
    }
    private List<Phone> mapToPhoneList(List<String> numbers) {
        int index = 0;
        List<Phone> phones = new ArrayList<>();
        for (var num : numbers) {
            phones.add(new Phone(null, null, index++, num));
        }
        return phones;
    }
}