package ru.otus.pro.hw.webServer.handlers;

import ru.otus.pro.hw.webServer.exceptions.BadRequestException;
import ru.otus.pro.hw.webServer.helpers.ApplicationPropertiesHelper;
import ru.otus.pro.hw.webServer.http.HttpContext;

public class ContentLengthExceededHandler implements HttpContextHandler {

    public ContentLengthExceededHandler() {

    }

    @Override
    public void execute(HttpContext context) {
        var maxContentLength = ApplicationPropertiesHelper.tryGet(ApplicationPropertiesHelper.MAX_CONTENT_LENGTH, Integer.class);

        throw new BadRequestException(String.format("Превышение разрешенной длины содержимого запроса. Отправлено %d. Разрешено к отправке %d", context.getRequest().getContentLength(), maxContentLength));
    }
}
