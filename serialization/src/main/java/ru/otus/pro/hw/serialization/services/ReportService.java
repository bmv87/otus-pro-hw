package ru.otus.pro.hw.serialization.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.otus.pro.hw.serialization.exceptions.ServiceException;
import ru.otus.pro.hw.serialization.models.in.SessionsContainer;
import ru.otus.pro.hw.serialization.models.in.SmsMessage;
import ru.otus.pro.hw.serialization.models.out.ChatSessionVM;
import ru.otus.pro.hw.serialization.models.out.MemberVM;
import ru.otus.pro.hw.serialization.models.out.NumberHistoryMV;
import ru.otus.pro.hw.serialization.models.out.SmsMessageVM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ReportService {

    private ImportService importService;

    public ReportService(ObjectMapper mapper, ImportService importService) {
        this.importService = importService;
    }


    public List<ChatSessionVM> getReport() {
        try {
            SessionsContainer result = importService.getChatSessions();
            var list = new ArrayList<ChatSessionVM>();
            for (var session : result.getSessions()) {
                var numbers = session.getMessages()
                        .stream()
                        .sorted((m1, m2) -> m2.getDate().compareTo(m1.getDate()))
                        .collect(
                                Collectors.groupingBy(SmsMessage::getBelongNumber,
                                        Collectors.mapping(m -> new SmsMessageVM(m.getBelongNumber(), m.getSendDate(), m.getText()),
                                                Collectors.toList())))
                        .entrySet()
                        .stream()
                        .map(g-> new NumberHistoryMV(g.getKey(), g.getValue()))
                        .toList();
                var newS = new ChatSessionVM();
                newS.setChatIdentifier(session.getChatIdentifier());
                newS.setMembers(session.getMembers().stream().map(m -> new MemberVM(m.getLast())).toList());
                newS.setNumbers(numbers);
                list.add(newS);
            }
            return list;
        } catch (IOException e) {
            throw new ServiceException("get report error", e);
        }

    }
}
